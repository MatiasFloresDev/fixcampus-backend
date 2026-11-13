# Documentación de FixCampus

Este conjunto acompaña la demo del proyecto para una entrevista de documentación. La regla es sencilla: lo que está funcionando se marca como implementado; lo demás queda como propuesta con criterios listos para discutir y priorizar.

El backend usa Java 21, como `PC2/demoSI/demoSI`. Configura el SDK del proyecto y JAVA_HOME con un JDK 21 antes de ejecutar `mvnw.cmd`. La estructura vigente y las pruebas se detallan en [Revisión de FixCampus con demoSI](Revision-backend-demoSI.md).

## Archivos

1. [Historias de Usuario](Historias-de-usuario-FixCampus.md) — backlog priorizado, criterios de aceptación, dependencias y estado.
2. [Casos de Uso](Casos-de-uso-FixCampus.md) — actores, flujos, alternativas y reglas de negocio.
3. [Prototipo](Prototipo-FixCampus.md) — mapa de pantallas, wireframes y decisiones de interfaz.
4. [Especificación funcional y técnica](Especificacion-funcional-tecnica-FixCampus.md) — alcance, permisos, API, datos, arquitectura y pruebas.
5. [Modelo entidad-relación](MER-FixCampus.md) — relaciones principales de la base de datos.
6. [Guion de entrevista](Guion-entrevista-FixCampus.md) — orden sugerido para explicar el proyecto y separar evidencia real de propuesta.
7. [Consultas e integración del equipo](Actualizacion-consultas-y-documento-2026-10-01.md) — diez consultas en cinco pares, ejemplos, procedencia y cambios de Yair.
8. [Verificación de la entrega](Revision-entrega-consultas-yair-2026-10-01.md) — requisitos del backend comprobados y evidencias pendientes de la entrega.

9. [Lombok y lambdas de clase](Lombok-y-lambdas-demoSI.md) — apunte histórico de septiembre. La versión actual usa getters y setters explícitos.

10. [Consultas nuevas de Yair](Integracion-consultas-Yair-2026-10-02.md) — integración en main, explicación, ejemplos y comprobaciones locales.

## Cómo presentarlo en cinco minutos

1. Explica el problema: los avisos informales pierden ubicación, categoría y seguimiento.
2. Muestra HU-03 y CU-01: son el mismo flujo visto desde dos niveles de detalle.
3. Demuestra en Swagger registro, login, categorías, ubicaciones, creación y consulta de reportes propios.
4. Enseña Swagger: contrato `POST /api/reports`, respuesta `201` y permisos.
5. Cierra con HU-06, HU-09 y HU-11 como evolución: evidencia, responsable/SLA y línea de tiempo.

## Nota de autoría

Antes de la entrevista, reemplaza cualquier frase genérica por tu aporte real y agrega fechas o enlaces de tus evidencias. No atribuyas al proyecto una prueba, reunión o decisión que no puedas explicar.

## Demo de sugerencia de categoría

La opción **Sugerir categoría con IA** aparece en **Nueva incidencia**. Angular envía solo el campo
`descripcion` a Spring cuando pulsas el botón. Spring consulta Gemini 3.1 Flash-Lite con el
catálogo de categorías y devuelve un ID válido o `null`. El formulario permite usar la sugerencia
y cambiarla antes de guardar la incidencia.

1. Genera una clave en [Google AI Studio](https://aistudio.google.com/apikey) con un proyecto del
   nivel gratuito, sin vincular facturación. Usa descripciones ficticias, sin datos personales.
2. En PowerShell, desde la carpeta del backend, carga la clave para esa terminal y arranca Spring:

   ```powershell
   $env:GEMINI_API_KEY = [System.Net.NetworkCredential]::new('', (Read-Host 'Clave de Gemini' -AsSecureString)).Password
   .\mvnw.cmd spring-boot:run
   ```

   Si ejecutas Spring desde IntelliJ, configura `GEMINI_API_KEY` en las variables de entorno de la
   ejecución. Conserva la clave fuera del repositorio y de la configuración de Angular.
3. Abre **Nueva incidencia** con una sesión de FixCampus y prueba con un ejemplo ficticio,
   como «Se apagó una lámpara del pasillo». El catálogo debe contener una categoría pertinente.
4. En Swagger, el contrato es `POST /api/reports/sugerir-categoria` con JWT de `ADMIN` o `USUARIO`
   y un cuerpo como `{"descripcion":"Se apagó una lámpara del pasillo"}`.
   Una cuota agotada devuelve `429`. Sin clave o sin conexión, la consulta devuelve `503`.
   La selección manual está disponible en todos esos casos.
