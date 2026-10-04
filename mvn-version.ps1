# Align the platform reactor and the consuming Identity service without publishing anything.
# mvn-version.ps1 -Version x.x.x-SNAPSHOT
[CmdletBinding(SupportsShouldProcess)]
param(
    [Parameter(Mandatory)]
    [ValidatePattern('^\d+\.\d+\.\d+(-[A-Za-z0-9]+([.-][A-Za-z0-9]+)*)?$')]
    [string]$Version
)
$ErrorActionPreference = 'Stop'
$rootPom = Join-Path $PSScriptRoot 'pom.xml'
[xml]$root = Get-Content -LiteralPath $rootPom -Raw
$paths = @($rootPom) + @($root.project.modules.module | ForEach-Object {
    Join-Path $PSScriptRoot "$_/pom.xml"
})
$changes = @{}
foreach ($path in $paths) {
    $content = Get-Content -LiteralPath $path -Raw -Encoding UTF8
    # Only concrete versions belonging to this platform; leave third-party versions untouched.
    $content = [regex]::Replace($content,
        '(<groupId>it\.asansonne</groupId>\s*<artifactId>[^<]+</artifactId>\s*<version>)[^<$]+(</version>)',
        [System.Text.RegularExpressions.MatchEvaluator]{ param($m) $m.Groups[1].Value + $Version + $m.Groups[2].Value })
    if ($path -eq $rootPom) {
        $content = [regex]::Replace($content, '<revision>[^<]+</revision>', "<revision>$Version</revision>")
    }
    [xml]$validated = $content
    $changes[$path] = $content
}
$servicePom = Join-Path $PSScriptRoot 'services/identity-service/pom.xml'
$content = Get-Content -LiteralPath $servicePom -Raw -Encoding UTF8
if ($content -notmatch '<platform.version>[^<]+</platform.version>') {
    throw 'The People POM must declare platform.version.'
}
$content = [regex]::Replace($content, '<platform.version>[^<]+</platform.version>', "<platform.version>$Version</platform.version>")
[xml]$validated = $content
$changes[$servicePom] = $content
if ($PSCmdlet.ShouldProcess("Platform reactor and People dependency ($($changes.Count) POMs)", "Set version $Version")) {
    foreach ($path in $changes.Keys) {
        [IO.File]::WriteAllText($path, $changes[$path], [Text.UTF8Encoding]::new($false))
    }
    Write-Output "Platform version set to $Version. No artifacts published."
}
