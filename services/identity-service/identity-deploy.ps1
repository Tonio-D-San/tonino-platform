# Dev: .\identity-deploy.ps1
# Prod: .\identity-deploy.ps1 -Profile prod
# Build pulita: .\identity-deploy.ps1 -Profile prod -NoCache
# Deploy + log: .\identity-deploy.ps1 -Profile prod -FollowLogs

param(
    [ValidateSet("dev", "prod")]
    [string]$Profile = "dev",

    [switch]$NoCache,

    [switch]$FollowLogs
)

$ErrorActionPreference = "Stop"

$Root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $Root

$EnvFile = ".env.$Profile"
$BaseCompose = "compose.yml"
$ProfileCompose = "compose.$Profile.yml"

foreach ($file in @($EnvFile, $BaseCompose, $ProfileCompose)) {
    if (-not (Test-Path $file)) {
        throw "File mancante: $file"
    }
}

$ComposeArgs = @(
    "--env-file", $EnvFile,
    "-f", $BaseCompose,
    "-f", $ProfileCompose
)

Write-Host "==> Validazione configurazione Docker Compose ($Profile)..."
docker compose @ComposeArgs config --quiet
if ($LASTEXITCODE -ne 0) {
    throw "docker compose config fallito."
}

Write-Host "==> Build identity-service..."
$BuildArgs = @("build")
if ($NoCache) {
    $BuildArgs += "--no-cache"
}
$BuildArgs += "identity-service"

docker compose @ComposeArgs @BuildArgs
if ($LASTEXITCODE -ne 0) {
    throw "Build identity-service fallita."
}

Write-Host "==> Deploy identity-service ($Profile)..."
docker compose @ComposeArgs up -d --no-build identity-service
if ($LASTEXITCODE -ne 0) {
    throw "Deploy identity-service fallito."
}

Write-Host "==> Stato container..."
docker compose @ComposeArgs ps identity-service

if ($FollowLogs) {
    Write-Host "==> Log identity-service..."
    docker compose @ComposeArgs logs -f identity-service
}
else {
    Write-Host ""
    Write-Host "Deploy completato."
    Write-Host "Per seguire i log:"
    Write-Host "docker compose --env-file $EnvFile -f $BaseCompose -f $ProfileCompose logs -f identity-service"
}
