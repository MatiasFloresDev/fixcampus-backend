# FixCampus: actualización del 01/10/2026

Integración del backend en `matias-flores`. La autoría indicada es la registrada en Git; no equivale a una asignación de Trello ni demuestra quién escribió cada línea fuera de Git.

## Correcciones del documento indicadas en clase

Lista basada en la revisión previa del audio. Los tiempos son aproximados. Este archivo contiene las indicaciones; esta actualización del código no modifica el documento de Google Docs.

1. **Student Outcome 3: comunicación efectiva**, reemplazando Outcome 2. Usar el texto del Anexo A del enunciado. Cada integrante explica individualmente sus aportes de comunicación escrita y oral con ejemplos reales. Alrededor de 02:34–02:37.
2. Llamar **TB1** a la entrega de semana 7, en lugar de TP. No renombrar otras entregas sin consultar el enunciado.
3. Corregir `4.2.X` por la numeración del sprint: Sprint 1 en `4.2.1`. Añadir fechas reales de inicio y fin. Sprint 1 comprende semanas 1–4; Sprint 2, semanas 5–6.
4. En Sprint Backlog se puede colocar el **enlace de Trello** con historias, tareas y responsables en vez de duplicar tablas vacías.
5. Sprint 1: evidencias de **CRUD, GitHub, participación individual, pruebas Swagger/Postman, diagrama de BD, documentación y ejecución local**. Render corresponde al segundo sprint.
6. Añadir Sprint 2 con la estructura completa de evidencias del primero hasta Team Collaboration, cambiando el contenido: **consultas, Spring Security y despliegue Render**. Alrededor de 02:39–02:42.
7. Team Collaboration: participación real de cada integrante y valoración correspondiente; se pueden incluir capturas de commits, Contributors y Network.
8. Describir pasos de configuración/despliegue y mostrar resultados comprobables.
9. Para esta entrega, dejar sin desarrollar **Validation Interviews**, diseño y registro de esas entrevistas, evaluación heurística y videos About the Product/About the Team. La guía de estilos no es obligatoria. Mantener conclusiones, recomendaciones, bibliografía y anexos pertinentes. Alrededor de 02:42–02:43.
10. Alinear documento y Trello: cinco integrantes requieren diez consultas, **una simple de una tabla y otra con JOIN más COUNT/SUM/AVG por integrante**. Los permisos por rol van en criterios y tareas; cada historia debe tener responsable y terminar con pruebas. Alrededor de 02:23–02:33.

## Cambios traídos de las ramas

| Autor registrado | Cambios | Referencias |
|---|---|---|
| Jeffrey De la Cruz | Q7: total por categoría, incluidas categorías con cero reportes; ruta explícita para la búsqueda por descripción Q6 | `e46e0de`, `235895a` |
| Mauricio1608 | Q10: recomendación por ID del reporte; respuesta 404 cuando no tiene una registrada | `76b4a31`, `5477018` |
| Lender06 (Yair) | Interfaces/repositorios con prefijo I; carpeta servicesimplements; clases ServiceImplement; carpeta configs; formato y constructores de entidades | `d567e50` |
| Matias Gerard Flores Flores | Integración, resolución de conflictos, sustitución de lambdas y adaptación a SQL por columnas | `b4de012`, `1044960`, `dfa21d2` |

Se conservaron `@NotBlank` y `@Size` de Rol, que el refactor retiraba. Las novedades se integraron sin streams ni lambdas y manteniendo el listado de usuarios sin parámetros. Los conteos y recomendaciones requieren ADMIN. No se reincorporaron los archivos físicos ni el borrado automático de registros asociados.

## Diez consultas funcionales

Todas se prueban con GET y un token obtenido en `POST /login`. En Swagger, pegarlo en **Authorize**. Base local: `http://localhost:8080`. Los IDs deben corresponder a registros existentes.

| Q | Qué hace / tipo | Autor registrado en Git | Ruta y ejemplo |
|---|---|---|---|
| 1 | Filtra incidencias por estado; simple | Matias inicialmente (`b5b759c`); José también trabajó el repositorio de reportes | `/api/reports?estado=ABIERTO` |
| 2 | Cuenta incidencias de cada usuario por año y mes; JOIN, GROUP BY y COUNT | Matias (`472e19b`) | `/api/reports/estadisticas/por-usuario-mes` |
| 3 | Busca ubicaciones por parte del campus; simple | José (`6a90b79`), sobre una consulta inicialmente de Matias (`ec488ea`) | `/api/locations?campus=Monterrico` |
| 4 | Filtra incidencias por prioridad; simple | José Ponce (`27395a9`) | `/api/reports/prioridad/ALTA` |
| 5 | Busca incidencias por campus exacto; JOIN | José Ponce (`27395a9`) | `/api/reports/campus/Monterrico` |
| 6 | Busca categorías por parte de la descripción; simple | Jeffrey (`45a7fa8`) | `/api/categories/buscar-descripcion?palabraClave=luces` |
| 7 | Cuenta reportes por categoría, incluyendo categorías sin reportes; LEFT JOIN y COUNT | Jeffrey (`e46e0de`) | `/api/categories/reporte-por-categoria` |
| 8 | Filtra recomendaciones de mantenimiento por prioridad; simple | Mauricio (`b88cc7c`) | `/api/recomendaciones/prioridad?prioridad=ALTA` |
| 9 | Busca recomendaciones por la categoría de la incidencia; JOIN | Mauricio (`b88cc7c`) | `/api/recomendaciones/por-categoria?nombre=Electricidad` |
| 10 | Busca la recomendación de una incidencia; relación por FK | Mauricio: endpoint (`5477018`); el método auxiliar ya existía en `f32f774` | `/api/recomendaciones/reporte/1` |

Se conservaron las dos consultas seleccionadas para Matias (Q1 y Q2) y ocho existentes con aportes de sus compañeros. La numeración de Swagger y repositorios ahora va de 1 a 10. Se retiraron once consultas y cuatro DTO de estadísticas sin uso; los ocho CRUD siguen disponibles.

### Cómo explicar las consultas

- **Q1:** `WHERE` compara el estado de la incidencia; devuelve sus datos, sin agrupar. Ejemplo: `ABIERTO` muestra incidencias pendientes de atención.
- **Q2:** une reporte con usuario mediante la FK; obtiene año y mes de la fecha de creación; `GROUP BY` reúne usuario, año y mes; `COUNT` cuenta reportes. Si Ana creó tres incidencias en septiembre y una en octubre, aparecen dos filas con cantidades 3 y 1. Devuelve `usuarioId`, `nombre`, `apellido`, `anio`, `mes` y `cantidad`. No necesita parámetros.
- **Q3:** compara el campus con un texto parcial, sin distinguir mayúsculas. `Monterrico` permite encontrar las ubicaciones de ese campus.
- **Q4:** compara la prioridad del reporte. `ALTA` permite revisar incidencias urgentes.
- **Q5:** relaciona reporte con ubicación y compara el campus completo. `Monterrico` devuelve incidencias de ese campus; no calcula un total.
- **Q6:** busca un texto dentro de la descripción de la categoría. `luces` identifica categorías cuya descripción contiene esa palabra.
- **Q7:** parte de categoría y hace `LEFT JOIN` con reporte. `GROUP BY` reúne cada categoría y `COUNT` cuenta los reportes existentes. Electricidad con dos incidencias devuelve `totalReportes: 2`; una categoría sin incidencias devuelve `totalReportes: 0`. Devuelve `idCategoria`, `nombre`, `descripcion` y `totalReportes`. No necesita parámetros.
- **Q8:** compara la prioridad sugerida en la recomendación de mantenimiento. `ALTA` permite revisar las recomendaciones urgentes.
- **Q9:** relaciona recomendación, reporte y categoría, y compara el nombre de categoría. `Electricidad` muestra sus recomendaciones de mantenimiento; no es una agregación.
- **Q10:** busca por la FK del reporte. Usar un ID obtenido en `GET /api/reports`; devuelve una recomendación o `404` si no existe.

Las dos agrupaciones (Q2 y Q7) devuelven filas `Object[]`. El servicio convierte cada columna en el campo de su DTO con ciclos `for`. El orden de las posiciones coincide con el `SELECT`.

### Métodos auxiliares y requisito individual

Los métodos de login, registro, acceso a reportes propios, consulta de adjuntos de un reporte, conteo de usuarios y operaciones de `JpaRepository` siguen disponibles: son dependencias de los CRUD y de la seguridad, no nuevas consultas numeradas del catálogo académico. `GET /api/users` lista todos los usuarios sin parámetros ni contraseñas.

La autoría proviene del historial de Git, no de una asignación inventada. Yair tiene aportes de refactor, pero no una nueva consulta identificada. Hay diez consultas en total y dos agrupaciones con COUNT; esto **no acredita una simple y otra con agregación por cada uno de los cinco integrantes**. Q5 y Q9 usan JOIN pero no COUNT/SUM/AVG. Conservar las ocho existentes fue la selección solicitada; el equipo debe contrastar el reparto con la profesora y Trello.

## JWT HS512 y clave

Se siguió `demoSI_seguridad`: la clave utiliza `HmacSHA512`; el token se firma con `MacAlgorithm.HS512` y el decodificador acepta HS512. `JwtConfig` comprueba que la clave tenga al menos **64 bytes** al convertirla a UTF-8. Una clave corta detiene el arranque con un mensaje claro.

`application.properties` contiene una clave local de prueba de 64 caracteres ASCII (64 bytes). `JWT_SECRET`, si está definida, reemplaza ese valor. En Render se configura una clave privada de al menos 64 bytes en **Environment → JWT_SECRET**; no se deben copiar las claves de prueba. Esta modificación local no cambia la variable ni despliega Render.

512 bits equivalen a 64 bytes; contar caracteres solo asegura esa longitud si son ASCII. El algoritmo firma el token, no cifra su contenido. Las contraseñas siguen usando BCrypt. Los tokens HS256 anteriores dejan de ser válidos: iniciar sesión otra vez después de reiniciar el backend.

## Comprobaciones

Maven compiló y empaquetó el proyecto; las cinco pruebas automatizadas pasaron. Las pruebas HTTP sobre PostgreSQL local verificaron **53/53 operaciones de Swagger, 116 peticiones, ocho CRUD y las diez consultas**. Se comprobó el token HS512, su firma de 64 bytes, el rechazo de HS256 y firmas alteradas, y los permisos ADMIN/USUARIO. Los registros temporales se eliminaron y los conteos originales se restauraron.

La revisión de los 73 archivos Java no encontró lambdas, referencias a métodos, ternarios ni streams. Son resultados del código local; no confirman un nuevo despliegue de Render.
