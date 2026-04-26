param(
    [string]$ContainerName = "rpg_mysql",
    [string]$DatabaseName = "choose_your_fate",
    [string]$Username = "root",
    [string]$Password = "123456",
    [string]$BackupDirectory = $PSScriptRoot
)

$ErrorActionPreference = "Stop"

if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    throw "Docker is not installed or is not available in PATH."
}

if (-not (Test-Path -LiteralPath $BackupDirectory)) {
    New-Item -ItemType Directory -Path $BackupDirectory | Out-Null
}

$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$backupFile = Join-Path $BackupDirectory "$DatabaseName`_$timestamp.sql"

docker exec $ContainerName mysqldump "--user=$Username" "--password=$Password" `
    --databases $DatabaseName `
    --routines `
    --triggers `
    --events `
    --single-transaction `
    --add-drop-database `
    --add-drop-table `
    --result-file="/tmp/$DatabaseName`_$timestamp.sql"

docker cp "$ContainerName`:/tmp/$DatabaseName`_$timestamp.sql" $backupFile
docker exec $ContainerName rm "/tmp/$DatabaseName`_$timestamp.sql" | Out-Null

Write-Host "Database backup created:"
Write-Host $backupFile
