# CLAUDE.md

Claude debe seguir primero las instrucciones de [AGENTS.md](AGENTS.md), que contiene el contexto del proyecto, los servicios, las reglas de Git y cómo validar cambios.

## Reglas específicas para Claude
- Antes de empezar una tarea, confirma en qué rama estás (`git branch --show-current`). Si estás en `main`, crea una rama nueva con `git switch -c nombre-de-la-tarea`.
- No hagas commit ni push a `main`, ni merge de PR.
- Solo implementa código funcional. No tomes capturas de pantalla ni generes archivos de evidencia (esas las hace el equipo manualmente). Sí puedes ejecutar pruebas para verificar que el código funciona.
- Antes de cambiar código, lee los archivos relacionados y respeta su estilo actual.
- Al terminar, resume qué archivos cambiaste y qué comandos ejecutaste para validar. Indica si no pudiste validar algo.
- Ante la duda sobre el alcance de la tarea, pregunta antes de modificar otros servicios.
