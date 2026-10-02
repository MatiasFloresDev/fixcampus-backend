# Integración de consultas de Yair — 02/10/2026

Se integra en main el commit dd36bce575619ee6fc4fed8a2c34f51dc22028e9, «Agrega consultas de usuarios», cuyo autor registrado es Lender06 (Yair). Se conservan sus commits originales. La rama yair-refactor no se modifica ni recibe publicaciones de esta integración.

## Consultas incorporadas

| Consulta | Ruta GET | Qué hace | Ejemplo |
|---|---|---|---|
| 9, simple | /api/users/nombre | Busca usuarios por nombre exacto, distinguiendo mayúsculas; no devuelve contraseñas | nombre=Matias; si no hay coincidencias devuelve [] |
| 10, un JOIN + COUNT | /api/users/por-rol | Relaciona usuario y rol por id_rol, agrupa por nombre de rol y cuenta usuarios; ordena de mayor a menor | Sin parámetros; USUARIO con ocho usuarios devuelve nombreRol=USUARIO y cantidadUsuarios=8 |

Las dos rutas requieren un token ADMIN. Un usuario sin ese rol recibe 403. Para probarlas: POST /login, copiar el token a Authorize y ejecutar la ruta. La segunda consulta usa INNER JOIN: no incluye roles sin usuarios. Cuenta usuarios de cualquier estado.

Se sustituyen las dos consultas provisionales de comentarios. Continúan exactamente diez consultas: cinco simples y cinco con un JOIN y COUNT. El CRUD de comentarios permanece completo. GET /api/users continúa sin parámetros; el filtro por nombre tiene su propia ruta.

## Ajustes de compatibilidad

- Se resolvieron los conflictos de UsuarioController, IUsuarioRepository e IUsuarioService conservando registro público, DTO de entrada/salida y seguridad de main.
- Se corrigieron dos referencias a usuarioRepository: el atributo de la implementación actual se llama repository.
- Las conversiones de las nuevas consultas usan ciclos for. No se añaden streams ni lambdas; las cinco lambdas permitidas permanecen exclusivamente en SecurityConfig.
- La consulta SQL conserva su lógica y utiliza cadenas concatenadas. UsuariosPorRolDTO usa Getter/Setter de Lombok, como los demás DTO; sustituye a ComentariosPorReporteDTO.

## Validación local

Maven clean verify terminó correctamente: compilación, empaquetado y una prueba contextLoads aprobada, sin errores ni fallos. Continúa una sola clase de prueba en el proyecto.

Contra PostgreSQL local se comprobaron 54/54 operaciones de Swagger mediante 124 peticiones: los ocho CRUD, registro/login, las diez consultas con valores esperados, permisos ADMIN/USUARIO, rechazo de JWT HS256 o firma alterada y protección de claves foráneas. Para las consultas nuevas se comprobó una búsqueda exacta, una búsqueda sin resultados y conteos por rol antes y después de crear usuarios temporales. Los datos temporales se eliminaron; los conteos iniciales de las ocho tablas y los grupos de usuarios por rol se restauraron.

La revisión de los 78 archivos Java confirma cinco lambdas, todas en SecurityConfig, cero streams, cero referencias a métodos y cero ternarios. Continúan 26 DTO y ocho entidades. Estas comprobaciones corresponden al entorno local; no acreditan un despliegue de esta versión en Render.
