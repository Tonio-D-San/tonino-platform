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
                APP_ID = 'gestionale'; KEYCLOAK_REALM_NAME = 'gestionale'
                KEYCLOAK_CLIENT_ID = 'gestionale-api'; KC_ADMIN_CLIENT_ID = 'gestionale-be'
                KEYCLOAK_APP_CLIENT_ID = 'gestionale-fe'; PLATFORM_BACKEND_CLIENT_SECRET = 'test-"secret\value'
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
    if ($Method -eq 'Get' -and $path -eq '/gestionale') {
        if (!$identityRealmTestState.realmCreated) {
            $error = [Exception]::new('Not found')
            $error | Add-Member Response ([pscustomobject]@{ StatusCode = 404 })
            throw $error
        }
        return @{ realm = 'gestionale' }
    }
    if ($Method -eq 'Post' -and $path -eq '') {
        $identityRealmTestState.realmCreated = $true
        $identityRealmTestState.realmCreates++
        return
    }
    if ($Method -eq 'Get' -and $path -eq '/gestionale/client-scopes') {
        return @($identityRealmTestState.clientScopes.Values)
    }
    if ($Method -eq 'Post' -and $path -eq '/gestionale/client-scopes') {
        $data | Add-Member id $data.name
        $identityRealmTestState.clientScopes[$data.name] = $data
        return
    }
    if ($path -match '^/gestionale/client-scopes/(gestionale-api-audience)$') {
        $name = $matches[1]
        if ($Method -eq 'Get') { return $identityRealmTestState.clientScopes[$name] }
        if ($Method -eq 'Put') { $identityRealmTestState.clientScopes[$name] = $data; return }
    }
    if ($Method -eq 'Get' -and $path -match '^/gestionale/clients\?clientId=(.+)$') {
        $id = [uri]::UnescapeDataString($matches[1])
        if ($id -eq 'realm-management') { return [pscustomobject]@{ id = 'management'; clientId = $id } }
        if ($identityRealmTestState.clients.ContainsKey($id)) { return $identityRealmTestState.clients[$id] }
        return
    }
    if ($Method -eq 'Post' -and $path -eq '/gestionale/clients') {
        $data | Add-Member id $data.clientId
        $identityRealmTestState.clients[$data.clientId] = $data
        return
    }
    if ($path -match '^/gestionale/clients/(gestionale-(?:api|be|fe))$') {
        $id = $matches[1]
        if ($Method -eq 'Get') { return $identityRealmTestState.clients[$id] }
        if ($Method -eq 'Put') { $identityRealmTestState.clients[$id] = $data; return }
    }
    if ($Method -eq 'Get' -and $path -like '/gestionale/groups?*') { return $identityRealmTestState.groups }
    if ($Method -eq 'Post' -and $path -eq '/gestionale/groups') { $identityRealmTestState.groups += $data; return }
    if ($path -eq '/gestionale/clients/gestionale-be/service-account-user') { return @{ id = 'backend-account' } }
    if ($path -eq '/gestionale/clients/management/roles/manage-users') { return @{ id = 'manage-users'; name = 'manage-users' } }
    if ($path -eq '/gestionale/users/backend-account/role-mappings/clients/management') {
        if ($Method -eq 'Get') { return $identityRealmTestState.roles }
        if ($Method -eq 'Post') { $identityRealmTestState.roles += @($data); return }
    }
    throw "Unexpected API call: $Method $path"
}
$setup = Join-Path $PSScriptRoot '../Initialize-IdentityRealm.ps1'
$credential = [PSCredential]::new('test-admin', (ConvertTo-SecureString 'test-password' -AsPlainText -Force))
& $setup -EnvFile 'mock.env' -AdminCredential $credential
$frontend = $identityRealmTestState.clients['gestionale-fe']
$frontend.redirectUris += 'https://custom.example.com/callback'
$frontend.defaultClientScopes += 'custom-scope'
$frontend.attributes | Add-Member 'custom-setting' 'keep'
$frontend.protocolMappers += [pscustomobject]@{ id = 'custom'; name = 'custom-mapper' }
$scope = $identityRealmTestState.clientScopes['gestionale-api-audience']
$scope.protocolMappers += [pscustomobject]@{ id = 'custom-scope-mapper'; name = 'custom-scope-mapper' }
& $setup -EnvFile 'mock.env' -AdminCredential $credential
if ($identityRealmTestState.realmCreates -ne 1 -or $identityRealmTestState.clients.Count -ne 3 -or
    $identityRealmTestState.clientScopes.Count -ne 1 -or $identityRealmTestState.groups.Count -ne 2 -or
    $identityRealmTestState.roles.Count -ne 1) {
    throw 'Repeated provisioning created duplicate resources.'
}
$frontend = $identityRealmTestState.clients['gestionale-fe']
if ($frontend.redirectUris -notcontains 'https://custom.example.com/callback' -or
    $frontend.defaultClientScopes -notcontains 'custom-scope' -or
    $frontend.defaultClientScopes -notcontains 'gestionale-api-audience' -or
    $frontend.attributes.'custom-setting' -ne 'keep' -or
    !($frontend.protocolMappers | Where-Object { $_.name -eq 'custom-mapper' })) {
    throw 'Provisioning lost custom client settings.'
}
$scope = $identityRealmTestState.clientScopes['gestionale-api-audience']
$audienceMapper = $scope.protocolMappers | Where-Object { $_.name -eq 'gestionale-api-audience' } | Select-Object -First 1
if (!$scope -or
    !($scope.protocolMappers | Where-Object { $_.name -eq 'custom-scope-mapper' }) -or
    @($scope.protocolMappers | Where-Object { $_.name -eq 'gestionale-api-audience' }).Count -ne 1 -or
    $audienceMapper.config.'included.client.audience' -ne 'gestionale-api') {
    throw 'Provisioning lost custom client scope settings or duplicated audience mapper.'
}
if ($identityRealmTestState.clients['gestionale-be'].defaultClientScopes -notcontains 'gestionale-api-audience') {
    throw 'Backend client is missing the API audience client scope.'
}
if ($identityRealmTestState.clients['gestionale-be'].secret -ne 'test-"secret\value') { throw 'Secret JSON escaping failed.' }
Write-Output 'PASS: create, repeat, preserve custom settings, service-account grant and secret escaping.'
