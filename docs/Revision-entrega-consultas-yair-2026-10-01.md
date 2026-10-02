# Verificación del backend frente a la entrega y retroalimentación

Se contrastó esta actualización con el enunciado del trabajo final disponible, los ejemplos demoSI/demoSI_seguridad y las indicaciones de código registradas en la revisión previa del audio de clase. El enunciado general también abarca entregas posteriores; no se presenta como una matriz de calificación específica de TB1 ni se asegura una nota.

| Requisito del backend de esta entrega | Resultado / evidencia |
|---|---|
| Ocho tablas y sus CRUD | Ocho entidades, repositorios, servicios e interfaces; ocho controladores de entidades. Las pruebas HTTP cubren crear, listar, consultar por ID, actualizar y eliminar |
| Registro y login | POST /registro en UsuarioController; POST /login en LoginController; ambas rutas públicas. Contraseña mínima de seis caracteres, BCrypt y JWT |
| Autenticación y autorización | HS512 con clave mínima de 64 bytes; roles ADMIN/USUARIO. Rechazo de token inválido, HS256, firma alterada y acceso sin permisos |
| Protección de información de usuarios | GET /api/users sin parámetros; las respuestas no contienen contraseña ni hash |
| Diez consultas / dos por integrante | Cinco pares documentados: una consulta de una tabla y una con exactamente un JOIN más COUNT por par. Los responsables asignados se distinguen de la autoría en Git |
| Tema FixCampus | Incidencias, campus, categorías, usuarios reportantes, recomendaciones manuales de mantenimiento y comentarios; filtros/indicadores pertinentes |
| Código compatible con la restricción solicitada | 78 fuentes Java: únicamente cinco lambdas de SecurityConfig presentes en demoSI_seguridad; sin streams, referencias a métodos ni ternarios; conversión de DTO con for |
| Configuración y carga inicial | Solo ModelMapperConfig en configs; seguridad en securities; sin DataInitializer ni carga automática de registros |
| Documentación de endpoints | Swagger: 54 operaciones y descripciones de Consulta 1 a Consulta 10, sin números repetidos |
| Pruebas y lógica | Maven clean verify: una prueba básica de arranque aprobada; PostgreSQL local: 124 peticiones, ocho CRUD, diez consultas, agrupaciones de varios registros y conteos de cero; datos temporales eliminados |
| Historial del equipo | Integración previa de main 46949df y yair-refactor 9445874, más dd36bce de Yair el 02/10 en main; commits originales preservados; correcciones de compatibilidad documentadas |

## Lo que esta verificación no acredita por sí sola

- El informe Word/Google Docs y Trello necesitan reflejar estos pares y responsabilidades. Esta tarea actualizó los documentos del repositorio, sin modificar esas aplicaciones.
- Para la revisión individual, cada integrante debe explicar y evidenciar su trabajo. Q4 se adaptó aquí; Q8 se añadió aquí y se asignó para el reparto. Q9 y Q10 provienen del commit dd36bce de Yair / Lender06. La asignación no crea evidencia de autoría previa.
- El enunciado del trabajo final incluye interfaz web, otros servicios y requisitos de entregas posteriores. Esta actualización se limita al backend y respeta las restricciones expresas del usuario para esta etapa; no certifica la totalidad del trabajo final.
- La prueba de PostgreSQL local no acredita que Render esté ejecutando la nueva versión. Revisar el despliegue, JWT_SECRET de al menos 64 bytes, variables de BD, Swagger publicado y capturas reales para evidenciarlo.

La exposición debe incluir un filtro simple y una consulta agrupada por integrante, explicando tablas, FK del JOIN, GROUP BY, COUNT, parámetros y resultado; además un CRUD, autenticación y diferencia entre 401 y 403.

Lombok se incorporó posteriormente por pedido del usuario: Getter/Setter en las ocho entidades y 26 DTO; los constructores se mantienen. Ver [explicación y referencias](Lombok-y-lambdas-demoSI.md).
