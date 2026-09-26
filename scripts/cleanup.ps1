$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
Remove-Item (Join-Path $root 'target') -Recurse -Force -ErrorAction SilentlyContinue
Write-Output 'VERIFICADO: artefactos Maven eliminados; la base de datos no fue modificada.'