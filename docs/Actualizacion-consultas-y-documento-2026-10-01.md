# FixCampus: actualización del 01/10/2026

Integración del backend para `main`, `matias-flores` y `yair-refactor`. La autoría indicada es la registrada en Git; no equivale a una asignación de Trello ni demuestra quién escribió cada línea fuera de Git.

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

## Integración de las ramas

Se sincronizaron `main` hasta `46949df` y `yair-refactor` hasta `9445874`, conservando los commits y sus autores originales. Los últimos commits de Yair, registrados como Lender06, son `f7e6dc2` (refactor antes de controllers), `df0881d` (refactor completo) y `9445874` (correcciones finales).

Se incorporaron sus DTO separados de entrada/salida para adjuntos, comentarios, recomendaciones, roles, ubicaciones y salida de usuario; también constructores, getters/setters y nombres explícitos de columnas en entidades. Los controladores de Rol y Ubicacion convierten los DTO mediante ModelMapper, con ciclos `for` como alternativa solicitada a streams.

Se resolvieron diferencias que reintroducían lambdas/streams, cambiaban `password` por `contrasena`, buscaban un rol incompatible con los datos existentes y modificaban permisos o el borrado de registros asociados. Registro conserva sus cuatro campos y asigna USUARIO; login mantiene `correo` y `password`. La seguridad firma/valida HS512 y conserva las restricciones ADMIN/USUARIO.

Se conserva la lógica validada: nueva incidencia ABIERTO, autor obtenido del token, listado de usuarios sin parámetros ni contraseña, fechas de asignación/resolución y eliminación con FK protegida. No se incorporan archivos físicos, datos de prueba al arrancar ni cambios de la aplicación cliente.

La integración no adopta cada modificación de Yair sin ajustes: las variantes incompatibles con los requisitos se corrigieron. Los cambios de sus entidades y DTO, además de sus commits originales, permanecen en el resultado integrado. La procedencia de una consulta se distingue de su responsable asignado en la tabla siguiente.

## Diez consultas: cinco pares

El reparto siguiente identifica al responsable de explicar y mantener cada par. **Asignación no significa autoría histórica**: los commits originales permanecen en Git. Q4 se adaptó en esta actualización y Q8, Q9 y Q10 se implementaron aquí; no se atribuyen retrospectivamente a un compañero.

Todas se ejecutan con GET en Swagger. Obtener un token en `POST /login` y pegarlo en **Authorize**. Las estadísticas y consultas administrativas requieren ADMIN. Q5 admite también USUARIO.

| Responsable del par | Q | Tipo y finalidad | Ruta / parámetros de ejemplo | Procedencia |
|---|---|---|---|---|
| Matias | 1 | Simple: listar incidencias por estado | `/api/reports?estado=ABIERTO` | Aporte previo de Matias (`b5b759c`) |
| Matias | 2 | Un JOIN + COUNT: incidencias por usuario, año y mes | `/api/reports/estadisticas/por-usuario-mes`; sin parámetros | Aporte previo de Matias (`472e19b`) |
| José | 3 | Simple: listar incidencias por prioridad | `/api/reports/prioridad/ALTA` | Aporte previo de José (`27395a9`) |
| José | 4 | Un JOIN + COUNT: cantidad de incidencias por campus de un estado | `/api/reports/estadisticas/por-campus?estado=ABIERTO` | Adaptada aquí a un JOIN; asignada a José |
| Jeffrey | 5 | Simple: categorías cuya descripción contiene una palabra | `/api/categories/buscar-descripcion?palabraClave=luces` | Aporte previo de Jeffrey (`45a7fa8`) |
| Jeffrey | 6 | Un LEFT JOIN + COUNT: incidencias por categoría, incluidas categorías sin incidencias | `/api/categories/reporte-por-categoria`; sin parámetros | Aporte previo de Jeffrey (`e46e0de`) |
| Mauricio | 7 | Simple: recomendaciones por prioridad sugerida | `/api/recomendaciones/prioridad?prioridad=ALTA` | Aporte previo de Mauricio (`b88cc7c`) |
| Mauricio | 8 | Un JOIN + COUNT: recomendaciones según la prioridad de la incidencia | `/api/recomendaciones/estadisticas/por-prioridad-reporte`; sin parámetros | Nueva en esta actualización; asignada a Mauricio |
| Yair | 9 | Simple: comentarios que contienen un texto | `/api/comments/buscar-texto?texto=lampara` | Nueva en esta actualización; asignada a Yair |
| Yair | 10 | Un LEFT JOIN + COUNT: comentarios por incidencia, incluyendo cero | `/api/comments/estadisticas/por-reporte`; sin parámetros | Nueva en esta actualización; asignada a Yair |

Las consultas Q1, Q3, Q5, Q7 y Q9 consultan una sola tabla. Las consultas Q2, Q4, Q6, Q8 y Q10 tienen exactamente una cláusula JOIN y una agregación COUNT; no se encadenan dos JOIN.

### Estructura y ejemplos para explicar

- **Q1:** compara `estado` sin distinguir mayúsculas; devuelve los reportes coincidentes. Si se omite estado, el CRUD lista todos. `ABIERTO` permite revisar incidencias pendientes de atención.
- **Q2:** `reporte JOIN usuario` mediante `id_usuario_reportante`; `EXTRACT` obtiene año y mes; `GROUP BY` forma grupos por persona y periodo; `COUNT(*)` cuenta incidencias. Ana con tres incidencias en septiembre y una en octubre produce dos filas: cantidades 3 y 1. Devuelve `usuarioId`, `nombre`, `apellido`, `anio`, `mes`, `cantidad`.
- **Q3:** compara la prioridad del reporte. `ALTA` devuelve incidencias urgentes sin sumar ni agrupar.
- **Q4:** `reporte JOIN ubicacion` por `id_ubicacion`; `WHERE` filtra el estado solicitado; `GROUP BY campus` y `COUNT(id_reporte)` cuentan incidencias por campus. Con `estado=ABIERTO`, Monterrico con cuatro incidencias abiertas devuelve `campus: Monterrico, cantidad: 4`. Los campus sin coincidencias no aparecen.
- **Q5:** compara la descripción de categoría con un texto parcial, sin distinguir mayúsculas. `luces` busca las categorías relacionadas con iluminación.
- **Q6:** `categoria LEFT JOIN reporte` por `id_categoria`; agrupa cada categoría y cuenta `id_reporte`. El LEFT JOIN conserva las categorías sin incidencias; COUNT de la columna del reporte devuelve cero para ellas. Devuelve `idCategoria`, `nombre`, `descripcion`, `totalReportes`.
- **Q7:** filtra `prioridadSugerida` de recomendación. `ALTA` muestra recomendaciones de mantenimiento urgentes; la recomendación es registrada por el administrador.
- **Q8:** `recomendacion JOIN reporte` por `id_reporte`; agrupa por la prioridad real del reporte y cuenta recomendaciones. Dos recomendaciones asociadas a incidencias ALTA y una a MEDIA producen cantidades 2 y 1. Se usa la prioridad de la incidencia, que puede diferir de la sugerida. Devuelve `prioridad`, `cantidad`.
- **Q9:** el método `findByTextoComentarioContainingIgnoreCase` genera la búsqueda por una parte del texto de un comentario. `texto=lampara` encuentra textos que contengan esa palabra; la búsqueda ignora mayúsculas, pero no elimina acentos.
- **Q10:** `reporte LEFT JOIN comentario` por `id_reporte`; agrupa por ID y título y cuenta `id_comentario`. Una incidencia con dos comentarios devuelve `cantidad: 2`; otra sin comentarios devuelve `cantidad: 0`. Devuelve `reporteId`, `titulo`, `cantidad`.

Cada consulta está comentada en su repositorio y tiene una descripción breve en Swagger. Los resultados `Object[]` de las cinco agrupaciones se convierten a DTO en los servicios con ciclos `for`; cada posición coincide con el orden del SELECT. Estas conversiones mantienen ciclos for y no utilizan lambdas ni streams. Las cinco lambdas permitidas se encuentran únicamente en SecurityConfig, siguiendo demoSI_seguridad.

### Métodos auxiliares

Login, registro, CRUD, consulta de reportes propios, adjuntos de un reporte y conteo de usuarios siguen disponibles. Son operaciones necesarias para seguridad y CRUD; no se añaden al catálogo de diez consultas académicas. `GET /api/users` lista todos, sin parámetros y sin contraseña.

Se retiraron los filtros anteriores por campus de ubicación, reportes por campus sin agregación, recomendaciones por categoría con dos JOIN y recomendación por reporte. Sus ocho CRUD continúan funcionando; para leer una recomendación concreta se usa `GET /api/recomendaciones/{id}`.

## JWT HS512 y clave

Se siguió `demoSI_seguridad`: la clave utiliza `HmacSHA512`; el token se firma con `MacAlgorithm.HS512` y el decodificador acepta HS512. `JwtConfig` comprueba que la clave tenga al menos **64 bytes** al convertirla a UTF-8. Una clave corta detiene el arranque con un mensaje claro.

`application.properties` contiene una clave local de prueba de 64 caracteres ASCII (64 bytes). `JWT_SECRET`, si está definida, reemplaza ese valor. En Render se configura una clave privada de al menos 64 bytes en **Environment → JWT_SECRET**; no se deben copiar las claves de prueba. Esta modificación local no cambia la variable ni despliega Render.

512 bits equivalen a 64 bytes; contar caracteres solo asegura esa longitud si son ASCII. El algoritmo firma el token, no cifra su contenido. Las contraseñas siguen usando BCrypt. Los tokens HS256 anteriores dejan de ser válidos: iniciar sesión otra vez después de reiniciar el backend.

## Comprobaciones de esta versión

`mvnw.cmd clean verify` compila y empaqueta el backend conservando una sola prueba básica: FixcampusApplicationTests.contextLoads, con la estructura del ejemplo demoSI. La comprobación HTTP externa al código del proyecto verifica los ocho CRUD, registro/login, permisos, las diez consultas y protección por FK.

Sobre PostgreSQL local, se verificaron **54/54 operaciones documentadas y 121 peticiones HTTP**, comprobando datos y conteos, no solo respuestas 200. Se validaron agrupaciones con dos incidencias y categorías/incidencias con conteos de cero. Se rechazaron HS256 y firmas alteradas; se comprobó HS512 con clave local de 64 bytes. Los datos temporales se eliminaron y los conteos iniciales se restauraron.

La revisión de los **78 archivos Java** encuentra únicamente cinco lambdas, todas en SecurityConfig y presentes en el ejemplo de seguridad. No hay referencias a métodos, ternarios ni streams. Hay ocho entidades, ocho repositorios y ocho controladores de entidades más LoginController. `configs` contiene únicamente ModelMapperConfig. Hay 26 DTO utilizados: 16 de CRUD, dos de login, dos de registro, cinco de resultados agrupados y uno de errores.

Estos resultados validan la versión local. La publicación GitHub no confirma que Render ya ejecute la misma versión. Render necesita `JWT_SECRET` privada de al menos 64 bytes y sus variables de conexión a PostgreSQL.

Lombok se incorporó posteriormente por pedido del usuario: Getter/Setter en las ocho entidades y 26 DTO; los constructores se mantienen. Ver [explicación y referencias](Lombok-y-lambdas-demoSI.md).
