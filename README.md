# T2-Grupo-5-DSW-II
PAGOYA Peru fintech tarjetas prepago - ES T2 reclamos.

# Integrantes
- SAID EDERSON OYOLA ROQUE
- EVERT RATA MALDONADO
- BRAYAN ERAZO FUNES
- JUAN ALONSO ESTEBAN PAJUELO CHIPANA
-JHOSIAS URIEL PASMIÑO SALDAÑA


## Servicios
| Servicio | Puerto | Descripcion |
|---|---|---|
| eureka-server | 8761 | Descubrimiento Eureka |
| ms-reclamos | 8081 | Registro, consulta por numero y actualizacion de reclamos |
| ms-resolucion | 8082 | Resolucion via Feign, estado 2 (procedente) / 3 (improcedente) |

## Base de datos
`ms-reclamos` usa MySQL 8 en Docker Compose. Antes de arrancar los servicios, levantar la base:

```
cd database
docker compose up -d
```

- MySQL: `localhost:5510` (base `appdb`, usuario `app`, password `password`).
- phpMyAdmin: http://localhost:3410 (servidor `mysqldb`, usuario `app`, password `password`).

## Orden arranque
1. database (`docker compose up -d`, desde terminal)
2. eureka-server (desde IntelliJ)
3. ms-reclamos (desde IntelliJ)
4. ms-resolucion (desde IntelliJ)

Guia completa: docs/flujo-levantar-proyecto.md

## Paso 1 verificado
Eureka en http://localhost:8761 con MS-RECLAMOS y MS-RESOLUCION UP. Ver docs/evidencia/paso3-eureka-apps.xml.

## Endpoints
- POST http://localhost:8081/reclamos -> 201 con defaults (`estado=1`, `analista="No asignado"`, `observacion="por evaluar"`)
- GET http://localhost:8081/reclamos/{num} -> 200
- PUT http://localhost:8081/reclamos/{num} -> 200 (campos opcionales: `analista`, `estado`, `observacion`)
- PUT http://localhost:8082/resoluciones/{num} -> 200 (body obligatorio: `estado` 2 o 3, `analista` y `observacion` no vacios)
- PUT http://localhost:8082/resoluciones/999999 -> 404 reclamo no existe

Ejemplo de resolucion:
```json
{"estado":2,"analista":"Ana Riesgos","observacion":"cargo duplicado confirmado"}
```

## Postman
Importar `docs/postman/PAYGO-ES.postman_collection.json` y `docs/postman/PAYGO-Local.postman_environment.json`.
Seleccionar el entorno `PAYGO-Local` y ejecutar **Run collection**. La coleccion crea sus propios reclamos y verifica el flujo completo con tests.
