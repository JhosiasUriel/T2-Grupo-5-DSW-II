# T2-Grupo-5-DSW-II
PAGOYA Peru fintech tarjetas prepago - ES T2 reclamos.

## Servicios
| Servicio | Puerto | Descripcion |
|---|---|---|
| eureka-server | 8761 | Descubrimiento Eureka |
| ms-reclamos | 8081 | Registro, consulta por numero, actualizacion (pendiente) |
| ms-resolucion | 8082 | Resolucion via Feign, estado 2/3 (paso 3) |

## Orden arranque
1. eureka-server
2. ms-reclamos
3. ms-resolucion

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
