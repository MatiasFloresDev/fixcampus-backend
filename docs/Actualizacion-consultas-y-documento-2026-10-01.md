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
| Jeffrey De la Cruz | Q20: total por categoría, incluidas categorías con cero reportes; ruta explícita para la búsqueda por descripción Q15 | `e46e0de`, `235895a` |
| Mauricio1608 | Q21: recomendación por ID del reporte; respuesta 404 cuando no tiene una registrada | `76b4a31`, `5477018` |
| Lender06 (Yair) | Interfaces/repositorios con prefijo I; carpeta servicesimplements; clases ServiceImplement; carpeta configs; formato y constructores de entidades | `d567e50` |
| Matias Gerard Flores Flores | Integración, resolución de conflictos, sustitución de lambdas y adaptación a SQL por columnas | `b4de012`, `1044960`, `dfa21d2` |

Se conservaron `@NotBlank` y `@Size` de Rol, que el refactor retiraba. Las novedades se integraron sin streams ni lambdas y manteniendo el listado de usuarios sin parámetros. Los conteos y recomendaciones requieren ADMIN. No se reincorporaron los archivos físicos ni el borrado automático de registros asociados.

## Consultas funcionales numeradas

Todas las rutas se ejecutan con GET. Base local: `http://localhost:8080`. Obtener el token en POST `/login` y usar Authorize en Swagger. Reemplazar los IDs de ejemplo por los existentes.

| Q | Qué consulta / tipo | Autor registrado | Ruta y ejemplo |
|---|---|---|---|
| 1 | Categorías por parte del nombre; simple | Matias, `ec488ea` | `/api/categories?nombre=Electricidad` |
| 2 | Roles por parte del nombre; simple | Matias, `ec488ea` | `/api/roles?nombre=ADMIN` |
| 3 | Ubicaciones por campus parcial; simple | Matias inicialmente, `ec488ea`; José también lo trabajó, `6a90b79` | `/api/locations?campus=Monterrico` |
| 4 | Usuarios por estado; simple | Matias, `ec488ea` | `/api/users/estado?estado=ACTIVO` |
| 5 | Adjuntos por tipo; simple | Matias, `ec488ea` | `/api/attachments?tipoArchivo=pdf` |
| 6 | Comentarios de un reporte; relación FK | Matias, `ec488ea` | `/api/comments?reporteId=1` |
| 7 | Reportes por estado; simple | Matias inicialmente, `b5b759c`; José trabajó ReporteRepository | `/api/reports?estado=ABIERTO` |
| 8 | Reportes por nombre de categoría; JOIN | Matias inicialmente, `b5b759c`; José trabajó ReporteRepository | `/api/reports?categoria=Electricidad` |
| 9 | Reportes por correo reportante; JOIN | Matias, `472e19b` | `/api/reports?correo=usuario@fixcampus.com` |
| 10 | Cantidad por usuario/año/mes; JOIN y COUNT | Matias, `472e19b` | `/api/reports/estadisticas/por-usuario-mes` |
| 11 | Cantidad por campus y estado; JOIN y COUNT | Matias, `d865d57` | `/api/reports/estadisticas/por-campus?estado=ABIERTO` |
| 12 | Reportes por prioridad; simple | José Ponce, `27395a9` | `/api/reports/prioridad/ALTA` |
| 13 | Reportes por campus exacto; JOIN | José Ponce, `27395a9` | `/api/reports/campus/Monterrico` |
| 14 | Comentarios de un usuario por reporte; JOIN y COUNT | Matias, `d865d57` | `/api/comments/estadisticas/por-reporte?correo=usuario@fixcampus.com` |
| 15 | Categorías por descripción; simple | Jeffrey, `45a7fa8` | `/api/categories/buscar-descripcion?palabraClave=luces` |
| 16 | Recomendaciones por prioridad; simple | Mauricio, `b88cc7c` | `/api/recomendaciones/prioridad?prioridad=ALTA` |
| 17 | Recomendaciones por categoría del reporte; JOIN | Mauricio, `b88cc7c` | `/api/recomendaciones/por-categoria?nombre=Electricidad` |
| 18 | Cantidad por categoría entre fechas; JOIN y COUNT | Matias, `f32f774` | `/api/reports/estadisticas/por-categoria?desde=2026-09-01&hasta=2026-10-01` |
| 19 | Adjuntos en reportes de un usuario; JOIN, LEFT JOIN y COUNT | Matias, `f32f774` | `/api/attachments/estadisticas/por-usuario?correo=usuario@fixcampus.com` |
| 20 | Total por categoría incluyendo cero; LEFT JOIN y COUNT | Jeffrey, `e46e0de` | `/api/categories/reporte-por-categoria` |
| 21 | Recomendación del reporte; relación FK | Mauricio: endpoint `5477018`; el método auxiliar ya existía como lista en `f32f774` | `/api/recomendaciones/reporte/1` |

Q15 también se usa en `/api/categories?descripcion=luces`: son dos rutas para la misma consulta. Q9 se reutiliza en `/api/reports/mis-reportes`, obteniendo el correo del token.

Q18 incluye desde y excluye hasta. Q20 no filtra fechas e incluye categorías sin reportes: no es la misma consulta. Q21 devuelve una recomendación o 404; no es una agregación.

### Consultas auxiliares

- `IUsuarioRepository.findByCorreo`: login, validación del correo y usuario autenticado.
- `IRolRepository.findByNombre`: rol USUARIO para el registro público.
- `IRecomendacionRepository.existsByReporte_IdReporte`: evita dos recomendaciones para un reporte.
- `IAdjuntoRepository.findByReporte_IdReporte`: adjuntos de `/api/attachments/reporte/{reporteId}`, propietario o ADMIN.
- `JpaRepository.count`: total de usuarios en `/api/users/count`, ADMIN.
- `JpaRepository.findAll` y `findById`: listados y búsqueda por ID de los ocho CRUD. GET `/api/users` lista todos sin parámetros y sin contraseñas.

### Requisito individual

Hay seis agrupaciones con JOIN: Q10, Q11, Q14, Q18, Q19 y Q20. Q8, Q9, Q13 y Q17 relacionan tablas pero no cuentan, suman ni promedian.

El número total supera diez; eso no acredita un par por integrante. El aporte nuevo identificado de Yair es el refactor; no se encontró una consulta nueva atribuida a él. Las consultas Q13 de José y Q17 de Mauricio tampoco acreditan una agrupación. Contrastar la asignación y las evidencias reales en Trello; no inventar autorías para completar el reparto.

## Comprobaciones

Maven compiló y empaquetó el proyecto. Las cinco pruebas automatizadas pasaron, incluyendo conteos con cero, búsquedas nuevas, permisos y validación de Rol. Los 77 archivos Java no contienen lambdas, referencias a métodos, ternarios ni streams. Las pruebas HTTP con PostgreSQL local también pasaron y sus registros temporales se eliminaron. Estos resultados corresponden al código local; no confirman un despliegue nuevo de Render.
