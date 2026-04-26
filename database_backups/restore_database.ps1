param(
    [Parameter(Mandatory = $true)]
    [string]$BackupFile,

    [string]$ContainerName = "rpg_mysql",
    [string]$Username = "root",
    [string]$Password = "123456"
)

$ErrorActionPreference = "Stop"

if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    throw "Docker is not installed or is not available in PATH."
}

$resolvedBackup = (Resolve-Path -LiteralPath $BackupFile).Path
$containerBackup = "/tmp/restore_choose_your_fate.sql"

docker cp $resolvedBackup "$ContainerName`:$containerBackup"
docker exec $ContainerName mysql "--user=$Username" "--password=$Password" "--execute=source $containerBackup"
docker exec $ContainerName rm $containerBackup | Out-Null

Write-Host "Database restored from:"
Write-Host $resolvedBackup
