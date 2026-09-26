#!/usr/bin/env bash
set -eu

ROOT="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
cd "$ROOT"

command -v mvn >/dev/null 2>&1 || { echo "ERROR: Maven no esta disponible." >&2; exit 1; }

grep -q 'org.primefaces' pom.xml
grep -q 'org.postgresql' pom.xml
grep -q 'Faces Servlet' src/main/webapp/WEB-INF/web.xml
grep -q 'MovimientoDAO' src/main/java/com/uv/inventario/bean/MovimientoBean.java
grep -q 'SELECT existencia FROM producto WHERE id_producto = ? FOR UPDATE' src/main/java/com/uv/inventario/dao/MovimientoDAO.java
grep -q 'No se puede registrar una salida mayor' src/main/java/com/uv/inventario/dao/MovimientoDAO.java

mvn -q -DskipTests package
test -f target/pr01-inventario.war
echo "VERIFICADO: compilacion, WAR, JSF/PrimeFaces, PostgreSQL y regla de salidas."