<#
Creates missing resources and reconciles the Identity clients in an existing realm.
Uses Compose interpolation as the single source of names, URLs and secrets.
No realm/user deletion. Existing custom client mappers, attributes and redirects are retained.
#>
[CmdletBinding()]
param(
    [Parameter(Mandatory)][string]$EnvFile,
    [ValidateSet('dev', 'prod')][string]$Profile = 'dev',
    [PSCredential]$AdminCredential,
    [string]$AdminRealm = 'master',
    [switch]$Plan
)
$ErrorActionPreference = 'Stop'
$compose = Join-Path $PSScriptRoot '../identity-service/compose.yml'
$override = Join-Path $PSScriptRoot "../identity-service/compose.$Profile.yml"
$raw = & docker compose --env-file $EnvFile -f $compose -f $override config --format json
if ($LASTEXITCODE -ne 0) { throw 'Compose configuration failed.' }
$config = ($raw -join "`n") | ConvertFrom-Json
$vars = $config.services.keycloak.environment
if ($config.name -ne "tonino-platform-$Profile") {
    throw 'The selected Compose profile does not match the environment.'
}
$template = Get-Content (Join-Path $PSScriptRoot 'identity-service.json') -Raw
$rendered = [regex]::Replace($template, '\$\{([A-Z_]+)\}', {
    param($match)
    $name = $match.Groups[1].Value
    $value = $vars.$name
    if ([string]::IsNullOrWhiteSpace($value)) { throw "Missing template variable: $name" }
    # Escape inside the existing JSON string, including secrets containing quotes or backslashes.
    $json = ConvertTo-Json -InputObject ([string]$value) -Compress
    $json.Substring(1, $json.Length - 2)
})
$realm = $rendered | ConvertFrom-Json
if ($vars.APP_ID -notmatch '^[a-z][a-z0-9-]*$') { throw 'APP_ID must be a lowercase application identifier.' }
$clientIds = @($realm.clients | ForEach-Object { $_.clientId })
if (@($clientIds | Select-Object -Unique).Count -ne $clientIds.Count) { throw 'Client IDs must be distinct.' }
$clientScopeNames = @($realm.clientScopes | ForEach-Object { $_.name })
if ($clientScopeNames.Count -ne @($clientScopeNames | Select-Object -Unique).Count) {
    throw 'Client scope names must be distinct.'
}
foreach ($name in @('APPLICATION_URL', 'FRONTEND_URL', 'KC_HOSTNAME')) {
    $uri = $null
    if (![uri]::TryCreate($vars.$name, [UriKind]::Absolute, [ref]$uri) -or
        $uri.Scheme -notin @('http', 'https') -or $uri.Query -or $uri.Fragment -or $uri.UserInfo) {
        throw "$name must be an absolute HTTP(S) URL without credentials, query or fragment."
    }
}
Write-Output "Realm: $($realm.realm); clients: $($clientIds -join ', ')"
if ($Plan) {
    Write-Output 'Plan only: create missing realm/groups/client-scopes/clients; update managed settings; grant manage-users to the backend service account.'
    return
}
if (!$AdminCredential) { throw 'Supply -AdminCredential (Get-Credential) for the provisioning administrator, or use -Plan.' }
$baseUrl = $vars.KC_HOSTNAME.TrimEnd('/')
$adminRealmPath = [uri]::EscapeDataString($AdminRealm)
$token = Invoke-RestMethod -Method Post -Uri "$baseUrl/realms/$adminRealmPath/protocol/openid-connect/token" -Body @{
    grant_type = 'password'
    client_id = 'admin-cli'
    username = $AdminCredential.UserName
    password = $AdminCredential.GetNetworkCredential().Password
}
$headers = @{ Authorization = "Bearer $($token.access_token)" }
function Invoke-Admin([string]$Method, [string]$Path, $Body = $null) {
    $request = @{ Method = $Method; Uri = "$baseUrl/admin/realms$Path"; Headers = $headers }
    if ($null -ne $Body) {
        $request.ContentType = 'application/json; charset=utf-8'
        $request.Body = [Text.Encoding]::UTF8.GetBytes((ConvertTo-Json -InputObject $Body -Depth 100 -Compress))
    }
    # Enumerate JSON arrays before callers filter scopes, clients or groups.
    $response = Invoke-RestMethod @request
    return $response
}
$realmPath = '/' + [uri]::EscapeDataString($realm.realm)
try { $null = Invoke-Admin Get $realmPath }
catch {
    if ([int]$_.Exception.Response.StatusCode -ne 404) { throw }
    $definition = $realm | Select-Object * -ExcludeProperty clients, users, groups, clientScopes
    $null = Invoke-Admin Post '' $definition
}
foreach ($desired in @($realm.clientScopes)) {
    $found = @(Invoke-Admin Get "$realmPath/client-scopes")
    $existing = $found | Where-Object { $_.name -eq $desired.name } | Select-Object -First 1
    if (!$existing) {
        $null = Invoke-Admin Post "$realmPath/client-scopes" $desired
        continue
    }
    $existing = Invoke-Admin Get "$realmPath/client-scopes/$($existing.id)"
    foreach ($property in $desired.PSObject.Properties) {
        $name = $property.Name
        $value = $property.Value
        if ($name -eq 'protocolMappers') {
            $merged = @($existing.protocolMappers)
            foreach ($mapper in $value) {
                $previous = $merged | Where-Object { $_.name -eq $mapper.name } | Select-Object -First 1
                if ($previous -and $previous.id) { $mapper | Add-Member id $previous.id -Force }
                $merged = @($merged | Where-Object { $_.name -ne $mapper.name }) + @($mapper)
            }
            $value = @($merged | Where-Object { $null -ne $_ })
        } elseif ($name -eq 'attributes') {
            $attributes = @{}
            if ($existing.attributes) {
                foreach ($entry in $existing.attributes.PSObject.Properties) { $attributes[$entry.Name] = $entry.Value }
            }
            foreach ($entry in $value.PSObject.Properties) { $attributes[$entry.Name] = $entry.Value }
            $value = $attributes
        }
        $existing | Add-Member -MemberType NoteProperty -Name $name -Value $value -Force
    }
    $null = Invoke-Admin Put "$realmPath/client-scopes/$($existing.id)" $existing
}
foreach ($desired in $realm.clients) {
    $query = [uri]::EscapeDataString($desired.clientId)
    $found = @(Invoke-Admin Get "$realmPath/clients?clientId=$query")
    $existing = $found | Where-Object { $_.clientId -eq $desired.clientId } | Select-Object -First 1
    if (!$existing) {
        $null = Invoke-Admin Post "$realmPath/clients" $desired
        continue
    }
    $existing = Invoke-Admin Get "$realmPath/clients/$($existing.id)"
    foreach ($property in $desired.PSObject.Properties) {
        $name = $property.Name
        $value = $property.Value
        if ($name -eq 'protocolMappers') {
            $merged = @($existing.protocolMappers)
            foreach ($mapper in $value) {
                $previous = $merged | Where-Object { $_.name -eq $mapper.name } | Select-Object -First 1
                if ($previous -and $previous.id) { $mapper | Add-Member id $previous.id -Force }
                $merged = @($merged | Where-Object { $_.name -ne $mapper.name }) + @($mapper)
            }
            $value = @($merged | Where-Object { $null -ne $_ })
        } elseif ($name -eq 'attributes') {
            $attributes = @{}
            if ($existing.attributes) {
                foreach ($entry in $existing.attributes.PSObject.Properties) { $attributes[$entry.Name] = $entry.Value }
            }
            foreach ($entry in $value.PSObject.Properties) { $attributes[$entry.Name] = $entry.Value }
            $value = $attributes
        } elseif ($name -in @('redirectUris', 'webOrigins')) {
            $value = @((@($existing.$name) + @($value)) | Where-Object { $null -ne $_ } | Select-Object -Unique)
        } elseif ($name -in @('defaultClientScopes', 'optionalClientScopes')) {
            $value = @((@($existing.$name) + @($value)) | Where-Object { $null -ne $_ } | Select-Object -Unique)
        }
        $existing | Add-Member -MemberType NoteProperty -Name $name -Value $value -Force
    }
    $null = Invoke-Admin Put "$realmPath/clients/$($existing.id)" $existing
}
# Updating a client representation does not update its scope associations.
# Use the dedicated endpoints, adding managed scopes without removing custom ones.
$availableScopes = @(Invoke-Admin Get "$realmPath/client-scopes")
foreach ($desired in $realm.clients) {
    $query = [uri]::EscapeDataString($desired.clientId)
    $client = @(Invoke-Admin Get "$realmPath/clients?clientId=$query") |
        Where-Object { $_.clientId -eq $desired.clientId } | Select-Object -First 1
    foreach ($type in @('default', 'optional')) {
        $property = "${type}ClientScopes"
        if (!$desired.$property) { continue }
        $path = "$realmPath/clients/$($client.id)/$type-client-scopes"
        $assigned = @(Invoke-Admin Get $path)
        foreach ($name in $desired.$property) {
            $scope = $availableScopes | Where-Object { $_.name -eq $name } | Select-Object -First 1
            if (!$scope) { throw "Missing client scope: $name" }
            if (!($assigned | Where-Object { $_.id -eq $scope.id })) {
                $null = Invoke-Admin Put "$path/$($scope.id)"
            }
        }
    }
}
foreach ($group in $realm.groups) {
    $search = [uri]::EscapeDataString($group.name)
    $found = @(Invoke-Admin Get "$realmPath/groups?search=$search&exact=true")
    if (!($found | Where-Object { $_.name -eq $group.name })) {
        $null = Invoke-Admin Post "$realmPath/groups" $group
    }
}
$backendQuery = [uri]::EscapeDataString($vars.KC_ADMIN_CLIENT_ID)
$backend = @(Invoke-Admin Get "$realmPath/clients?clientId=$backendQuery") |
    Where-Object { $_.clientId -eq $vars.KC_ADMIN_CLIENT_ID } | Select-Object -First 1
$account = Invoke-Admin Get "$realmPath/clients/$($backend.id)/service-account-user"
$management = @(Invoke-Admin Get "$realmPath/clients?clientId=realm-management") |
    Where-Object { $_.clientId -eq 'realm-management' } | Select-Object -First 1
$role = Invoke-Admin Get "$realmPath/clients/$($management.id)/roles/manage-users"
$mappingPath = "$realmPath/users/$($account.id)/role-mappings/clients/$($management.id)"
$assigned = @(Invoke-Admin Get $mappingPath)
if (!($assigned | Where-Object { $_.id -eq $role.id })) {
    $null = Invoke-Admin Post $mappingPath @($role)
}
Write-Output 'Identity realm configuration applied. Existing users and unrelated resources retained.'
