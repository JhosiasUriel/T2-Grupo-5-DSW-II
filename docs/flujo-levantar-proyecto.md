# Flujo para levantar el proyecto y probarlo manualmente

La base de datos se levanta desde la terminal con Docker Compose. Los microservicios se levantan desde IntelliJ IDEA.

## 1. Requisitos previos

- Docker Desktop abierto y funcionando.
- IntelliJ IDEA con el JDK compatible con el proyecto (revisar `<java.version>` en el `pom.xml` de cada servicio).
- Puertos libres: `8761`, `8081`, `8082`, `5510`, `3410`.

## 2. Base de datos (MySQL + phpMyAdmin) desde la terminal

```powershell
cd database
docker compose up -d
docker compose ps
docker compose logs -f db
```

Esperar a ver `ready for connections` en los logs y luego presionar `Ctrl+C` (esto solo detiene el seguimiento de logs, no el contenedor).

- MySQL: `localhost:5510`, base `appdb`, usuario `app`, password `password`.
- phpMyAdmin: http://localhost:3410 (servidor `mysqldb`, usuario `app`, password `password`).

> Requiere el archivo `database/.env` con las variables `DB_IMAGE`, `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWD`, `DB_IMAGE_PHPMA`, `DB_HOST_PHPMA` y `DB_PORT_PHPMA`. Su contenido está en `docs/plan-db-mysql-docker.md`.

## 3. Abrir el proyecto en IntelliJ

1. File → Open → seleccionar la carpeta `T2-Grupo-5-DSW-II`.
2. Esperar a que IntelliJ importe los Maven projects (`eureka-server`, `ms-reclamos`, `ms-resolucion`). Si no los detecta, abrir el panel Maven y usar "Reload All Maven Projects".

## 4. Eureka Server (Run desde IntelliJ)

1. Abrir `eureka-server/src/main/java/com/pagoya/eureka/EurekaServerApplication.java`.
2. Click derecho sobre la clase → **Run 'EurekaServerApplication'**.
3. Verificar en http://localhost:8761. El dashboard debe cargar sin servicios registrados todavía.

## 5. ms-reclamos (Run desde IntelliJ)

1. Abrir `ms-reclamos/src/main/java/com/pagoya/reclamos/ReclamosApplication.java`.
2. Click derecho → **Run 'ReclamosApplication'**.

Debe conectarse a MySQL, crear la tabla `reclamos` (por `ddl-auto=update`) y registrarse en Eureka como `MS-RECLAMOS`.

## 6. ms-resolucion (Run desde IntelliJ)

1. Abrir `ms-resolucion/src/main/java/com/pagoya/resolucion/ResolucionApplication.java`.
2. Click derecho → **Run 'ResolucionApplication'**.

Debe registrarse en Eureka como `MS-RESOLUCION`.

> Orden de arranque: Eureka → ms-reclamos → ms-resolucion. Conviene dejar cada uno en su propia pestaña de Run para ver sus logs por separado.

Con los tres servicios arriba, http://localhost:8761 debe mostrar `MS-RECLAMOS` y `MS-RESOLUCION` como `UP`.

## 7. Pruebas manuales

La forma recomendada es con Postman, usando la colección `docs/postman/PAYGO-ES.postman_collection.json` y el entorno `docs/postman/PAYGO-Local.postman_environment.json`.

1. Importar la colección y el entorno en Postman.
2. Seleccionar el entorno `PAYGO-Local` (arriba a la derecha).
3. Click derecho sobre la colección `PAYGO-ES` → **Run collection** → **Run PAYGO-ES**, con el orden por defecto.

La colección ejecuta este flujo:

1. **P1 - Eureka:** verifica el dashboard y que `MS-RECLAMOS` y `MS-RESOLUCION` estén registrados.
2. **P2 - Reclamos:** registra dos reclamos (guarda sus IDs en `reclamoId1` y `reclamoId2`), consulta el primero y lo actualiza.
3. **P3 - Resolucion:** resuelve el reclamo 1 como procedente (`estado` 2) y el reclamo 2 como improcedente (`estado` 3), verifica cada cambio con un `GET` y comprueba que `PUT /resoluciones/999999` devuelva 404.

Cada petición tiene tests (`pm.test`). En la pestaña **Test Results** del runner se ve cuáles pasan y cuáles fallan.

> Los IDs de reclamo se generan automáticamente, así que la colección no depende de que la tabla esté vacía.

Persistencia (manual): detener `ReclamosApplication` en IntelliJ, volver a ejecutarlo y consultar `GET http://localhost:8081/reclamos/{reclamoId1}`. Debe seguir existiendo. También se puede verificar en phpMyAdmin, tabla `reclamos`.

> Nota: el README todavía muestra `PUT /resoluciones/{num}` con solo `{"estado":2}`. Ese body ya no funciona porque `analista` y `observacion` son obligatorios; la colección usa el body correcto.

> Las evidencias (capturas, exportes de Eureka, archivos en `docs/evidencia/`) las genera el equipo manualmente, según `AGENTS.md`.

## 8. Pruebas automatizadas (opcional)

En IntelliJ, click derecho sobre la carpeta de cada servicio → **Run 'All Tests'**. También se puede ejecutar desde el panel Maven con el goal `test`.

## 9. Puntos a verificar antes de arrancar

- `database/.env` existe y tiene las variables `DB_*`.
- `ms-reclamos/pom.xml` usa `mysql-connector-j` (no H2).
- `ReclamoClient` en `ms-resolucion` apunta a `MS-RECLAMOS` (por Eureka) o a una URL fija como `localhost:8081`. Si es una URL fija, cambiar puertos la rompe.
- Versión de Java y de Spring Boot en cada `pom.xml`, y que IntelliJ use ese mismo JDK en Project Structure.

## 10. Apagado

- Detener cada servicio con el botón **Stop** (cuadrado rojo) de IntelliJ.
- Detener la base desde la terminal: `cd database` y luego `docker compose down` (conserva los datos del volumen). Usar `docker compose down -v` solo si se quiere borrar la base.
