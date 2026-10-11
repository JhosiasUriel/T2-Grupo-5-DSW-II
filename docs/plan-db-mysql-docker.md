# Plan de ejecución: migrar la base de datos a MySQL 8 en Docker Compose

## Objetivo

Reemplazar la base H2 en memoria de `ms-reclamos` por un MySQL 8 en Docker Compose,
con el mismo estilo que usa el profesor en su monorepo
(`DESARROLLO-DE-APLICACIONES-WEB-II-04697-/database/docker-compose.yml` y
`application.yml` con `spring.datasource`).

Con esto los reclamos persisten entre reinicios y el esquema se sigue generando solo con
`ddl-auto=update` (Hibernate crea/actualiza la tabla `reclamos`).

## Estado actual

- `ms-reclamos` usa H2 en memoria: `jdbc:h2:mem:reclamos;DB_CLOSE_DELAY=-1`.
- El driver H2 está en `ms-reclamos/pom.xml` (scope `runtime`).
- `ms-resolucion` **no tiene base de datos**: consulta y actualiza reclamos por Feign contra
  `ms-reclamos`. No se toca.
- `eureka-server` no usa base de datos. No se toca.
- No existe `docker-compose.yml` en el repo de T2.

## Referencia del profesor

Config real del profesor (`database/.env`):

```properties
DB_IMAGE=mysql:8.0
DB_HOST=mysqldb
DB_PORT=5510
DB_NAME=appdb
DB_USER=app
DB_PASSWD=password
DB_PORT_PHPMA=3410
```

Y su `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:5510/appdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Lima
    username: app
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

> Nota: el repo del equipo en T1 (`T1-Grupo-5-DSW-II/database/docker-compose.yml`) ya usa
> MySQL + Docker Compose con `MYSQL_ROOT_PASSWORD` y `mysql_data`. Este plan se alinea con el
> profesor (variables en `.env`, usuario `app`), que es lo que se pidió.

## Decisiones acordadas

- Motor: **MySQL 8.0** (no PostgreSQL).
- Modo: **Docker Compose** con volumen nombrado para persistir datos.
- Puerto host: **5510** → 3306 (como el profesor), con phpMyAdmin en **3410**.
- Base: `appdb`, usuario `app`, password `password` (valores de curso, no de producción).
- Solo se modifica **`ms-reclamos`**. `ms-resolucion` y `eureka-server` quedan igual.

## Cambios a realizar (3 pasos)

### Paso 1 — Crear `database/docker-compose.yml` y `database/.env`

Crear la carpeta `database/` en la raíz del repo T2 y agregar:

**`database/.env`**

```properties
DB_IMAGE=mysql:8.0
DB_HOST=mysqldb
DB_PORT=5510
DB_NAME=appdb
DB_USER=app
DB_PASSWD=password
DB_IMAGE_PHPMA=phpmyadmin:5.2
DB_HOST_PHPMA=tsl.phpmyadmin
DB_PORT_PHPMA=3410
```

**`database/docker-compose.yml`**

```yaml
services:
  db:
    image: ${DB_IMAGE}
    container_name: ${DB_HOST}
    restart: always
    volumes:
      - my-db:/var/lib/mysql
    ports:
      - "${DB_PORT}:3306"
    environment:
      MYSQL_DATABASE: ${DB_NAME}
      MYSQL_USER: ${DB_USER}
      MYSQL_PASSWORD: ${DB_PASSWD}
      MYSQL_ROOT_PASSWORD: ${DB_PASSWD}
    networks:
      - net-mobile

  phpmyadmin:
    image: ${DB_IMAGE_PHPMA}
    container_name: ${DB_HOST_PHPMA}
    ports:
      - "${DB_PORT_PHPMA}:80"
    depends_on:
      - db
    networks:
      - net-mobile

volumes:
  my-db:

networks:
  net-mobile:
    name: net-mobile
```

> Si el equipo prefiere aislarse del profesor y evitar colisión de la red/volumen `my-db` y
> `net-mobile` con su infra, renombrar a `paygo-db` / `net-pagoya`. Por defecto se dejan como
> el profesor para máxima fidelidad.

### Paso 2 — Cambiar el driver en `ms-reclamos/pom.xml`

Reemplazar la dependencia de H2:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

por el conector de MySQL:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Paso 3 — Actualizar `ms-reclamos/src/main/resources/application.properties`

Reemplazar las propiedades de H2:

```properties
spring.datasource.url=jdbc:h2:mem:reclamos;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.h2.console.enabled=true
```

por las de MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:5510/appdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Lima
spring.datasource.username=app
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

Cambios clave:
- Se quita `spring.datasource.driver-class-name` (Spring lo infiere del conector).
- Se quita `spring.h2.console.enabled` (ya no aplica).
- Se mantiene `spring.jpa.hibernate.ddl-auto=update`.

## Orden de ejecución

1. Crear los archivos del Paso 1 (`database/.env` y `database/docker-compose.yml`).
2. Aplicar Paso 2 y Paso 3.
3. Levantar la base:

```powershell
cd "D:\Cibertec\6to ciclo\DAW II\T2\T2-Grupo-5-DSW-II\database"
docker compose up -d
docker compose ps
docker compose logs -f db
```

Esperar a que los logs digan `ready for connections`.

4. Arrancar los servicios en el orden de siempre: `eureka-server` → `ms-reclamos` → `ms-resolucion`.

## Verificación

1. `docker ps` debe mostrar `mysqldb` (5510) y `tsl.phpmyadmin` (3410) corriendo.
2. Abrir `http://localhost:3410`, entrar con servidor `mysqldb`, usuario `app`, password `password`,
   y ver la base `appdb` con la tabla `reclamos`.
3. Probar los endpoints ya documentados en el `README.md`:
   - `POST http://localhost:8081/reclamos` -> 201 con defaults.
   - `GET http://localhost:8081/reclamos/1` -> 200.
   - `PUT http://localhost:8082/resoluciones/1 {"estado":2}` -> procedente.
4. Reiniciar `ms-reclamos` y volver a consultar el reclamo: debe seguir existiendo (ya no se
   pierde como con H2 en memoria).

## Rollback

Si algo falla, revertir es volver al estado previo:
- En `application.properties`, restaurar las 3 líneas de H2 y `spring.h2.console.enabled=true`.
- En `pom.xml`, volver a la dependencia `com.h2database:h2`.
- Opcional: `docker compose down` (conserva el volumen) o `docker compose down -v` (borra datos).

## Notas de flujo de trabajo (según AGENTS.md)

- No trabajar directo en `main`: crear una rama, p. ej. `feat/db-mysql-docker`.
- Commit en Conventional Commits, en español, p. ej.:
  `feat: migrar ms-reclamos de H2 a MySQL 8 con Docker Compose`.
- Los `.env` de curso son de juguete; en un proyecto real no se suben credenciales.
- Actualizar el `README.md` (sección de orden de arranque) para indicar que primero se levanta
  `database/` con `docker compose up -d`.