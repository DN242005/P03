# Evidencia P03 - Control de inventario

Producto: `pr01-inventario.war`  
Relacion: P02/PR01 -> P03/M03 -> R03

## Indice de evidencia

| Evidencia | Criterio R03 | Estado | Como reproducir |
| --- | --- | --- | --- |
| E01 - Compilacion y WAR | Producto reconstruible | VERIFICADO | `./scripts/verify-module.sh M03` |
| E02 - Login y cierre de sesion | Control de sesion y trazabilidad | VERIFICADO | Abrir `/faces/login.xhtml`, seleccionar usuario y cerrar sesion |
| E03 - Alta y alerta de producto | Formulario, validacion y RF01/RF05/RF06 | PENDIENTE | Capturar formulario valido y consultar filtro de alertas |
| E04 - Entrada y salida valida | Persistencia RF02/RF03/RF04 | PENDIENTE | Registrar movimiento y revisar existencia/ultimos movimientos |
| E05 - Salida mayor a existencia | Caso negativo e integridad | PENDIENTE | Intentar salida mayor al stock; conservar mensaje de rechazo |
| E06 - Validacion de formulario | Mensajes y limites | PENDIENTE | Enviar campos obligatorios vacios y cantidad cero |

## Recorrido de demostracion

1. Ejecutar `psql -U postgres -f sql/schema.sql` y, opcionalmente, `psql -U postgres -d pr01_inventario -f sql/test-data.sql`.
2. Definir `DB_URL`, `DB_USER` y `DB_PASSWORD`, compilar y desplegar el WAR en Tomcat 9.
3. Iniciar sesion con `Ana Torres` o `Luis Perez`; `Coordinacion General` puede consultar, pero no registrar movimientos.
4. En Productos, validar un alta, consultar el catalogo y activar el filtro de alertas.
5. En Movimientos, registrar una entrada y una salida; comprobar la existencia y la bitacora reciente.
6. Repetir con una salida mayor a la existencia y conservar la captura del mensaje de error.

## Trazabilidad y limites

- P02/PR01 conserva catalogo, entradas, salidas, existencia transaccional, minimo y alertas.
- P03 agrega JSF/PrimeFaces, mensajes de validacion, sesion y ejecucion reproducible.
- RF08 (API REST/Angular), RF09 (IoT), facturacion y compras automaticas permanecen fuera de alcance.
- Los estados `PENDIENTE` requieren ejecutar PostgreSQL/Tomcat y adjuntar capturas del entorno de entrega.