# Offline contract test: mocks Docker and the Keycloak Admin API; no real credentials or network.
$ErrorActionPreference = 'Stop'
$identityRealmTestState = [pscustomobject]@{
    RealmCreated = $false; Clients = @{}; ClientScopes = @{}; Groups = @(); Roles = @(); RealmCreates = 0
}
function docker {
    $global:LASTEXITCODE = 0
    @{
        name = 'tonino-platform-dev'
        services = @{
            keycloak = @{ environment = @{
                APP_ID = 'identity-service'; KEYCLOAK_REALM_NAME = 'tonino-platform'
                KEYCLOAK_CLIENT_ID = 'identity-api'; KC_ADMIN_CLIENT_ID = 'identity-admin'
                KEYCLOAK_APP_CLIENT_ID = 'identity-swagger'; KC_ADMIN_CLIENT_SECRET = 'test-"secret\value'
                KEYCLOAK_CONSOLE_CLIENT_ID = 'identity-console'
                APPLICATION_URL = 'http://localhost:8082'; FRONTEND_URL = 'http://localhost:3000'
                KC_HOSTNAME = 'http://keycloak.localhost:5443'
            } }
            'identity-service' = @{ environment = @{ SPRING_PROFILES_ACTIVE = 'dev' } }
        }
    } | ConvertTo-Json -Depth 20
}
function Invoke-RestMethod {
    param($Method, $Uri, $Body, $Headers, $ContentType)
    if ($Uri -like '*/protocol/openid-connect/token') { return @{ access_token = 'test-token' } }
    $path = ([uri]$Uri).PathAndQuery -replace '^/admin/realms', ''
    $data = if ($Body -is [byte[]]) { [Text.Encoding]::UTF8.GetString($Body) | ConvertFrom-Json } else { $Body }
    if ($Method -eq 'Get' -and $path -eq '/tonino-platform') {
        if (!$identityRealmTestState.realmCreated) {
            $error = [Exception]::new('Not found')
            $error | Add-Member Response ([pscustomobject]@{ StatusCode = 404 })
            throw $error
        }
        return @{ realm = 'tonino-platform' }
    }
    if ($Method -eq 'Post' -and $path -eq '') {
        $identityRealmTestState.realmCreated = $true
        $identityRealmTestState.realmCreates++
        return
    }
    if ($Method -eq 'Get' -and $path -eq '/tonino-platform/client-scopes') {
        # Invoke-RestMethod emits a JSON array as one pipeline object.
        Write-Output -NoEnumerate @($identityRealmTestState.clientScopes.Values)
        return
    }
    if ($Method -eq 'Post' -and $path -eq '/tonino-platform/client-scopes') {
        $data | Add-Member id $data.name
        $identityRealmTestState.clientScopes[$data.name] = $data
        return
    }
    if ($path -match '^/tonino-platform/client-scopes/([^/]+)$') {
        $name = $matches[1]
        if ($Method -eq 'Get') { return $identityRealmTestState.clientScopes[$name] }
        if ($Method -eq 'Put') { $identityRealmTestState.clientScopes[$name] = $data; return }
    }
    if ($Method -eq 'Get' -and $path -match '^/tonino-platform/clients\?clientId=(.+)$') {
        $id = [uri]::UnescapeDataString($matches[1])
        if ($id -eq 'realm-management') { return [pscustomobject]@{ id = 'management'; clientId = $id } }
        if ($identityRealmTestState.clients.ContainsKey($id)) { return $identityRealmTestState.clients[$id] }
        return
    }
    if ($Method -eq 'Post' -and $path -eq '/tonino-platform/clients') {
        $data | Add-Member id $data.clientId
        $identityRealmTestState.clients[$data.clientId] = $data
        return
    }
    if ($path -match '^/tonino-platform/clients/(identity-(?:api|admin|swagger|console))$') {
        $id = $matches[1]
        if ($Method -eq 'Get') { return $identityRealmTestState.clients[$id] }
        if ($Method -eq 'Put') {
            # Keycloak ignores scope associations in a client update.
            $data | Add-Member defaultClientScopes $identityRealmTestState.clients[$id].defaultClientScopes -Force
            $identityRealmTestState.clients[$id] = $data
            return
        }
    }
    if ($path -match '^/tonino-platform/clients/(identity-(?:admin|swagger|console))/(default|optional)-client-scopes(?:/([^/]+))?$') {
        $id = $matches[1]
        $property = $matches[2] + 'ClientScopes'
        $scopeName = $matches[3]
        if ($Method -eq 'Get') {
            return @($identityRealmTestState.clients[$id].$property | ForEach-Object {
                [pscustomobject]@{ id = $_; name = $_ }
            })
        }
        if ($Method -eq 'Put' -and $scopeName) {
            $client = $identityRealmTestState.clients[$id]
            $client | Add-Member $property (@($client.$property) + @($scopeName) | Select-Object -Unique) -Force
            return
        }
    }
    if ($Method -eq 'Get' -and $path -like '/tonino-platform/groups?*') { return $identityRealmTestState.groups }
    if ($Method -eq 'Post' -and $path -eq '/tonino-platform/groups') { $identityRealmTestState.groups += $data; return }
    if ($path -eq '/tonino-platform/clients/identity-admin/service-account-user') { return @{ id = 'backend-account' } }
    if ($path -eq '/tonino-platform/clients/management/roles/manage-users') { return @{ id = 'manage-users'; name = 'manage-users' } }
    if ($path -eq '/tonino-platform/users/backend-account/role-mappings/clients/management') {
        if ($Method -eq 'Get') { return $identityRealmTestState.roles }
        if ($Method -eq 'Post') { $identityRealmTestState.roles += @($data); return }
    }
    throw "Unexpected API call: $Method $path"
}
$setup = Join-Path $PSScriptRoot '../Initialize-IdentityRealm.ps1'
$template = Get-Content -Raw (Join-Path $PSScriptRoot '../identity-service.json') | ConvertFrom-Json
$definedScopes = @($template.clientScopes | ForEach-Object { $_.name })
foreach ($client in $template.clients) {
    foreach ($name in $client.defaultClientScopes) {
        if ($definedScopes -notcontains $name) { throw "Realm import references an undefined scope: $name" }
    }
}
$basic = $template.clientScopes | Where-Object { $_.name -eq 'basic' }
if (!($basic.protocolMappers | Where-Object {
    $_.protocolMapper -eq 'oidc-sub-mapper' -and $_.config.'access.token.claim' -eq 'true'
})) { throw 'Access tokens must include the user subject.' }
$credential = [PSCredential]::new('test-admin', (ConvertTo-SecureString 'test-password' -AsPlainText -Force))
& $setup -EnvFile 'mock.env' -AdminCredential $credential
$frontend = $identityRealmTestState.clients['identity-swagger']
$frontend.redirectUris += 'https://custom.example.com/callback'
# Reproduce an existing realm whose clients only have the audience scope linked.
foreach ($id in @('identity-admin', 'identity-swagger', 'identity-console')) {
    $identityRealmTestState.clients[$id].defaultClientScopes = @('identity-api-audience')
}
$frontend.defaultClientScopes += 'custom-scope'
$frontend.attributes | Add-Member 'custom-setting' 'keep'
$frontend.protocolMappers += [pscustomobject]@{ id = 'custom'; name = 'custom-mapper' }
$scope = $identityRealmTestState.clientScopes['identity-api-audience']
$scope.protocolMappers += [pscustomobject]@{ id = 'custom-scope-mapper'; name = 'custom-scope-mapper' }
& $setup -EnvFile 'mock.env' -AdminCredential $credential
if ($identityRealmTestState.realmCreates -ne 1 -or $identityRealmTestState.clients.Count -ne 4 -or
    $identityRealmTestState.clientScopes.Count -ne 7 -or $identityRealmTestState.groups.Count -ne 2 -or
    $identityRealmTestState.roles.Count -ne 1) {
    throw 'Repeated provisioning created duplicate resources.'
}
$frontend = $identityRealmTestState.clients['identity-swagger']
if ($frontend.redirectUris -notcontains 'https://custom.example.com/callback' -or
    $frontend.defaultClientScopes -notcontains 'custom-scope' -or
    $frontend.defaultClientScopes -notcontains 'identity-api-audience' -or
    $frontend.attributes.'custom-setting' -ne 'keep' -or
    !($frontend.protocolMappers | Where-Object { $_.name -eq 'custom-mapper' })) {
    throw 'Provisioning lost custom client settings.'
}
$scope = $identityRealmTestState.clientScopes['identity-api-audience']
$audienceMapper = $scope.protocolMappers | Where-Object { $_.name -eq 'identity-api-audience' } | Select-Object -First 1
if (!$scope -or
    !($scope.protocolMappers | Where-Object { $_.name -eq 'custom-scope-mapper' }) -or
    @($scope.protocolMappers | Where-Object { $_.name -eq 'identity-api-audience' }).Count -ne 1 -or
    $audienceMapper.config.'included.client.audience' -ne 'identity-api') {
    throw 'Provisioning lost custom client scope settings or duplicated audience mapper.'
}
if ($identityRealmTestState.clients['identity-admin'].defaultClientScopes -notcontains 'identity-api-audience') {
    throw 'Backend client is missing the API audience client scope.'
}
if ($identityRealmTestState.clients['identity-admin'].secret -ne 'test-"secret\value') { throw 'Secret JSON escaping failed.' }
foreach ($clientId in @('identity-admin', 'identity-swagger', 'identity-console')) {
    foreach ($name in @('basic', 'profile', 'email', 'roles', 'web-origins', 'acr', 'identity-api-audience')) {
        if ($identityRealmTestState.clients[$clientId].defaultClientScopes -notcontains $name) {
            throw "Missing scope $name on $clientId."
        }
    }
}
Write-Output 'PASS: complete OIDC scopes, four clients, repeat, preserve custom settings, service-account grant and secret escaping.'
