# T2-Grupo-5-DSW-II
PAGOYA Peru fintech tarjetas prepago - ES T2 reclamos.

## Servicios
| Servicio | Puerto | Descripcion |
|---|---|---|
| eureka-server | 8761 | Descubrimiento Eureka |
| ms-reclamos | 8081 | Registro, consulta por numero, actualizacion (pendiente) |
| ms-resolucion | 8082 | Resolucion via Feign, estado 2/3 (paso 3) |

## Base de datos
`ms-reclamos` usa MySQL 8 en Docker Compose. Antes de arrancar los servicios, levantar la base:

```
cd database
docker compose up -d
```

- MySQL: `localhost:5510` (base `appdb`, usuario `app`, password `password`).
- phpMyAdmin: http://localhost:3410 (servidor `mysqldb`, usuario `app`, password `password`).

## Orden arranque
1. database (`docker compose up -d`)
2. eureka-server
3. ms-reclamos
4. ms-resolucion

## Paso 1 verificado
Eureka en http://localhost:8761 con MS-RECLAMOS y MS-RESOLUCION UP. Ver docs/evidencia/paso3-eureka-apps.xml.

## Endpoints verificados
- POST http://localhost:8081/reclamos -> 201 con defaults
- GET http://localhost:8081/reclamos/1 -> 200
- PUT http://localhost:8081/reclamos/1 -> 200
- PUT http://localhost:8082/resoluciones/1 {"estado":2} -> procedente
- PUT http://localhost:8082/resoluciones/2 {"estado":3} -> improcedente
- PUT http://localhost:8082/resoluciones/999999 -> 404 reclamo no existe

## Postman
Importar docs/postman/PAYGO-ES.postman_collection.json + PAYGO-Local.postman_environment.json
