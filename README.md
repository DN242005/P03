# PR01. Control de inventario inteligente

Proyecto integrador (Banco C11, EE Desarrollo de Sistemas Web, UV).
**Alcance de esta entrega: Web 1.0 (JSP/Servlets) + Web 2.0 (JSF/PrimeFaces).**
Web 3.0 (API REST + Angular) y Web 4.0 (simulacion IoT) quedan para incrementos posteriores (P03-P08); la tabla `lectura_sensor` ya esta en el esquema pero no se usa todavia.

## Problema y usuarios
Una organizacion pequena registra existencias de forma dispersa y detecta faltantes cuando ya afectan la operacion.
Usuarios: responsable de almacen, personal de captura y coordinacion.

## Alcance minimo cubierto en esta fase
- Catalogo de productos (RF01)
- Registro de entradas (RF02) y salidas (RF03)
- Existencia calculada de forma transaccional (RF04)
- Minimo permitido por producto (RF05)
- Consulta y alerta de stock bajo (RF06)
- Modelo relacional PostgreSQL: `producto`, `movimiento`, `categoria`, `usuario`, `lectura_sensor` (RF07, reservada)

RF08 (API Web 3.0 + Angular) y RF09 (evento IoT Web 4.0) **no** se implementan en esta entrega.

## Arquitectura
- **Web 1.0** (`/catalogo`, `/producto`, `/movimiento`): Servlets + JSP, renderizado en servidor, recarga completa de pagina.
- **Web 2.0** (`/faces/productos.xhtml`, `/faces/movimientos.xhtml`): JSF 2.3 (Mojarra) + PrimeFaces 8, componentes interactivos (`p:dataTable` con filtros/orden/paginacion, `p:selectOneMenu`, `p:inputNumber`, validacion de formulario con `required`).
- **Datos**: JDBC directo (sin ORM) contra PostgreSQL 16. La logica de negocio (bloquear salidas mayores a la existencia, actualizar existencia, autorizar por rol) vive en `MovimientoDAO` dentro de una transaccion (`SELECT ... FOR UPDATE`).
- Ambas capas comparten el mismo modelo y los mismos DAO — es el **mismo sistema evolucionando**, no dos aplicaciones distintas.

## Requisitos no funcionales atendidos
- **RNF01 Reproducibilidad**: ver pasos de ejecucion abajo; sin pasos secretos.
- **RNF02 Seguridad/privacidad**: credenciales de BD via variables de entorno (`DB_URL`, `DB_USER`, `DB_PASSWORD`), datos sinteticos, autorizacion por rol para registrar movimientos (`ALMACEN`/`CAPTURA`).
- **RNF03 Accesibilidad**: etiquetas `<label for>`, navegacion por teclado (`:focus` visible), mensajes de error/estado con `role="alert"`/`role="status"`, contraste AA en `estilo.css`.
- **RNF04 Integridad de datos**: llaves foraneas, `CHECK` de cantidades y roles, transaccion con bloqueo de fila para evitar condiciones de carrera al actualizar existencia.
- **RNF05 Trazabilidad**: la bitacora `movimiento` es de solo insercion (nunca se actualiza ni se borra).
- **RNF06 Despliegue acotado**: Tomcat 9 local + PostgreSQL local, sin nube ni servicios de pago.

## Riesgo principal y control
Alteracion de existencias → autorizacion por rol (`UsuarioDAO.puedeRegistrarMovimientos`) + bitacora inmutable de movimientos + transaccion que impide existencias negativas (`MovimientoDAO.registrar`).

## Requisitos para ejecutar
- JDK 11
- Apache Maven 3.8+
- Apache Tomcat 9 (soporta Servlet 4.0 / JSP requeridos por el `pom.xml`)
- PostgreSQL 16

También se puede usar Docker para PostgreSQL, sin instalar el servidor en el equipo local.

## Pasos de ejecucion (entorno limpio)

1. Crear la base de datos y cargar el esquema:
   ```bash
   psql -U postgres -f sql/schema.sql
   ```
   El script crea `pr01_inventario` si no existe, se conecta a ella y genera las tablas y datos iniciales.

   Con Docker:
   ```bash
   docker run --name postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16-alpine
   docker exec -i postgres psql -U postgres -d postgres < sql/schema.sql
   ```
   Si el contenedor ya existe, usa `docker start postgres` en lugar de `docker run`.

2. Definir las credenciales (no quedan escritas en el codigo):
   ```bash
   export DB_URL=jdbc:postgresql://localhost:5432/pr01_inventario
   export DB_USER=postgres
   export DB_PASSWORD=tu_password
   ```
   El archivo `.env.example` contiene estas variables como referencia; no se carga automaticamente y nunca debe sustituirse por credenciales reales dentro del repositorio.

3. Compilar el WAR:
   ```bash
   mvn clean package
   ```
   Esto requiere acceso a Maven Central para descargar Mojarra, PrimeFaces y el driver de PostgreSQL (no incluidos en este paquete de codigo fuente).

4. Desplegar `target/pr01-inventario.war` en Tomcat 9 (copiarlo a `webapps/`) y arrancar Tomcat con las mismas variables de entorno del paso 2 exportadas para el proceso de Tomcat.

5. Abrir:
   - Web 1.0: `http://localhost:8080/pr01-inventario/catalogo` y `http://localhost:8080/pr01-inventario/movimiento`
   - Web 2.0: `http://localhost:8080/pr01-inventario/faces/productos.xhtml` y `.../faces/movimientos.xhtml`

## Pruebas minimas (sugeridas para la evidencia P02)
- Positiva: registrar una entrada y verificar que la existencia aumenta.
- Positiva: registrar una salida valida y verificar que la existencia disminuye y aparece en "Ultimos movimientos".
- Negativa: intentar una salida mayor a la existencia → debe rechazarse con mensaje ("No se puede registrar una salida mayor a la existencia disponible").
- Negativa: capturar una cantidad de movimiento en cero o negativa → debe rechazarse.
- Recorrido principal: dar de alta un producto con minimo permitido, bajar su existencia por debajo del minimo con una salida, y confirmar que aparece marcado como "ALERTA" en ambos catalogos (Web 1.0 y Web 2.0).

## Exclusiones de esta ficha
Facturacion, compras automaticas, contabilidad y hardware obligatorio.

## Estructura del proyecto
```
sql/schema.sql                  Esquema PostgreSQL + datos sinteticos
src/main/java/.../model/        Entidades (POJO)
src/main/java/.../dao/          Acceso a datos y reglas de negocio (JDBC)
src/main/java/.../util/         Conexion a BD (credenciales externalizadas)
src/main/java/.../web1/         Servlets Web 1.0
src/main/java/.../bean/         Managed beans JSF Web 2.0
src/main/webapp/jsp/            Vistas Web 1.0
src/main/webapp/faces/          Vistas Web 2.0 (JSF/PrimeFaces)
src/main/webapp/resources/css/  Estilos compartidos
```
