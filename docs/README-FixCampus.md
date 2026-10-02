# Documentación de FixCampus

Este conjunto acompaña la demo del proyecto para una entrevista de documentación. La regla es sencilla: lo que está funcionando se marca como implementado; lo demás queda como propuesta con criterios listos para discutir y priorizar.

## Archivos

1. [Historias de Usuario](Historias-de-usuario-FixCampus.md) — backlog priorizado, criterios de aceptación, dependencias y estado.
2. [Casos de Uso](Casos-de-uso-FixCampus.md) — actores, flujos, alternativas y reglas de negocio.
3. [Prototipo](Prototipo-FixCampus.md) — mapa de pantallas, wireframes y decisiones de interfaz.
4. [Especificación funcional y técnica](Especificacion-funcional-tecnica-FixCampus.md) — alcance, permisos, API, datos, arquitectura y pruebas.
5. [Modelo entidad-relación](MER-FixCampus.md) — relaciones principales de la base de datos.
6. [Guion de entrevista](Guion-entrevista-FixCampus.md) — orden sugerido para explicar el proyecto y separar evidencia real de propuesta.
7. [Consultas e integración del equipo](Actualizacion-consultas-y-documento-2026-10-01.md) — diez consultas en cinco pares, ejemplos, procedencia y cambios de Yair.
8. [Verificación de la entrega](Revision-entrega-consultas-yair-2026-10-01.md) — requisitos del backend comprobados y evidencias pendientes de la entrega.

9. [Lombok y lambdas de clase](Lombok-y-lambdas-demoSI.md) — métodos generados, archivos y patrones de seguridad de demoSI_seguridad.

10. [Consultas nuevas de Yair](Integracion-consultas-Yair-2026-10-02.md) — integración en main, explicación, ejemplos y comprobaciones locales.

## Cómo presentarlo en cinco minutos

1. Explica el problema: los avisos informales pierden ubicación, categoría y seguimiento.
2. Muestra HU-03 y CU-01: son el mismo flujo visto desde dos niveles de detalle.
3. Demuestra en Swagger registro, login, categorías, ubicaciones, creación y consulta de reportes propios.
4. Enseña Swagger: contrato `POST /api/reports`, respuesta `201` y permisos.
5. Cierra con HU-06, HU-09 y HU-11 como evolución: evidencia, responsable/SLA y línea de tiempo.

## Nota de autoría

Antes de la entrevista, reemplaza cualquier frase genérica por tu aporte real y agrega fechas o enlaces de tus evidencias. No atribuyas al proyecto una prueba, reunión o decisión que no puedas explicar.
