# AGENTS.md

Instrucciones para agentes de IA que trabajen en este repositorio.

## Contexto del proyecto
- Proyecto: PAGOYA Perú (fintech de tarjetas prepago), módulo de reclamos. Curso DSW II, Grupo 5 (examen T2).
- Arquitectura de microservicios Spring Boot con Maven (cada servicio tiene su propio `pom.xml` y `mvnw`).

## Objetivo de la aplicación
Atender reclamos de clientes por consumos desconocidos en tarjetas prepago, con tres piezas que se comunican entre sí:

1. **Servidor de descubrimiento Eureka (4 pts):** Eureka Server que registra y monitorea los servicios REST de PAGOYA Perú.
2. **Servicio de reclamos (12 pts):** servicio REST de atención al cliente que gestiona reclamos.
   - Entidad `Reclamo` con los campos:
     - `numreclamo`
     - `fechareclamo`
     - `nomcliente`
     - `ultimos4tarjeta`
     - `motivo`
     - `nombre del analista`, por defecto `"No asignado"`
     - `estado`, por defecto `1` (pendiente)
     - `observación`, por defecto `"por evaluar"`
   - Métodos: registrar reclamo, consultar por número y actualizar reclamo. El método de actualización lo usa el servicio de resolución.
   - Debe registrarse en Eureka Server.
3. **Servicio de resolución (4 pts):** servicio REST para el área de riesgos que resuelve un reclamo.
   - Valida que el reclamo exista consultando al servicio de reclamos con OpenFeign.
   - Si existe, cambia `estado` a `2` (procedente) o `3` (improcedente).
   - Registra el nombre del analista.
   - Modifica la observación según el estado.
   - Muestra mensajes de éxito o error y debe registrarse en Eureka.

## Servicios
| Servicio | Puerto | Descripción |
|---|---|---|
| eureka-server | 8761 | Descubrimiento de servicios (Eureka) |
| ms-reclamos | 8081 | Registro, consulta por número y actualización de reclamos |
| ms-resolucion | 8082 | Resolución de reclamos vía Feign (estados 2 y 3) |

Orden de arranque: `eureka-server` → `ms-reclamos` → `ms-resolucion`.

## Estructura
- `eureka-server/`, `ms-reclamos/`, `ms-resolucion/`: código fuente de cada microservicio.
- `docs/`: documentación del proyecto.
  - `docs/postman/`: colección y entorno de Postman (`PAYGO-ES.postman_collection.json`, `PAYGO-Local.postman_environment.json`).
  - `docs/guia-git-equipo.md`: guía de flujo de trabajo con Git.

## Reglas de trabajo
- **Nunca trabajar ni hacer push directo a `main`.** Cada tarea va en su propia rama (`nombre-de-la-tarea`, en minúsculas y con guiones).
- Los cambios se integran a `main` solo mediante Pull Request revisado por otro integrante.
- No usar `git push --force` ni borrar ramas no mergeadas.
- Mensajes de commit en formato Conventional Commits (`feat:`, `fix:`, `docs:`, `style:`), en español.
- No modificar archivos fuera del alcance de la tarea.
- **Los agentes solo implementan código funcional.** No generan evidencias: ni capturas de pantalla, ni exportes de Eureka, ni archivos en `docs/evidencia/`. Esas evidencias las realiza el equipo manualmente.
- Los agentes sí pueden ejecutar pruebas y arranques locales para verificar que el código funciona, pero no deben guardar sus resultados como evidencia.
- No subir credenciales, claves ni archivos generados (`target/`, `.idea/`, `*.log`; ya están en `.gitignore`).

## Cómo validar cambios
- Compilar/probar un servicio con su wrapper: `./mvnw test` (o `mvnw.cmd test` en Windows) dentro de la carpeta del servicio.
- Los endpoints documentados en el `README.md` sirven como referencia de comportamiento esperado. Si cambias un endpoint, actualiza el README y la colección de Postman.

## Estilo
- Código y comentarios en el idioma que ya use el archivo; la documentación del proyecto está en español.
- Seguir la estructura y convenciones existentes de cada microservicio antes de introducir patrones nuevos.
