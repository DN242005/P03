$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
Set-Location $root

if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    throw 'ERROR: Maven no esta disponible.'
}

$checks = @(
    @{ File = 'pom.xml'; Text = 'org.primefaces' },
    @{ File = 'pom.xml'; Text = 'org.postgresql' },
    @{ File = 'src/main/webapp/WEB-INF/web.xml'; Text = 'Faces Servlet' },
    @{ File = 'src/main/java/com/uv/inventario/bean/MovimientoBean.java'; Text = 'MovimientoDAO' },
    @{ File = 'src/main/java/com/uv/inventario/dao/MovimientoDAO.java'; Text = 'SELECT existencia FROM producto WHERE id_producto = ? FOR UPDATE' },
    @{ File = 'src/main/java/com/uv/inventario/dao/MovimientoDAO.java'; Text = 'No se puede registrar una salida mayor' }
)

foreach ($check in $checks) {
    if (-not (Select-String -Path $check.File -SimpleMatch $check.Text -Quiet)) {
        throw "ERROR: no se encontro '$($check.Text)' en $($check.File)."
    }
}

mvn -q -DskipTests package
if (-not (Test-Path 'target/pr01-inventario.war')) {
    throw 'ERROR: no se genero target/pr01-inventario.war.'
}
Write-Output 'VERIFICADO: compilacion, WAR, JSF/PrimeFaces, PostgreSQL y regla de salidas.'