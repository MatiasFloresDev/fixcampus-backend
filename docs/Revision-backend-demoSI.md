# Revisión de FixCampus con los ejemplos de clase — 30/09/2026

## Referencias utilizadas

- `C:\Users\Matias\Downloads\demoSI\demoSI`: entidades, DTO, controladores, interfaces, servicios, repositorios, CRUD y consultas.
- `C:\Users\Matias\Downloads\demoSI_seguridad\demoSI`: BCrypt, AuthenticationManager, UserDetailsService, JWT, Swagger y autorización por roles.
- `microservicios.zip`: API independientes y Gateway. Su Gateway usa Java 8, Spring Boot 2.1.3 y Zuul; no se copian esas dependencias al proyecto actual, que comparte Java 17 y Spring Boot 4 con demoSI_seguridad.

## Qué se aplicó

| Conocimiento de clase | Aplicación en FixCampus |
|---|---|
| `@Entity`, `@Id`, `@GeneratedValue` | Ocho entidades y sus identificadores |
| `@ManyToOne`, `@OneToOne`, `@JoinColumn` | Relaciones de usuario, reporte, comentario, adjunto y recomendación |
| `JpaRepository` | CRUD de las ocho tablas |
| Interfaz + `@Service` | Una interfaz de negocio y una implementación por entidad |
| `@RestController`, mappings HTTP | Ocho controladores de entidades |
| `@RequestBody`, `@PathVariable`, `@RequestParam` | JSON de entrada, ID en la ruta y filtros de consultas |
| DTO y validaciones | Datos de entrada/salida; usuario de salida sin contraseña |
| `findBy...`, `@Query`, JOIN y GROUP BY | Búsquedas y estadísticas documentadas |
| SQL nativo + `List<Object[]>` | Dos agrupaciones convertidas a DTO mediante `for`; se evita usar streams o lambdas para recorrer sus resultados |
| BCrypt y JWT | Registro, login y acceso mediante Bearer token HS512 |
| `@PreAuthorize` | Permiso ADMIN/USUARIO por operación |
| OpenAPI | Swagger con descripciones de consultas y rutas públicas sin candado |

El recorrido es `Controller → Service (interfaz) → ServiceImpl → Repository → PostgreSQL`.
El controlador atiende HTTP y convierte la respuesta; el servicio aplica las reglas; el repositorio consulta o guarda; la entidad representa la tabla.

No se usan lambdas, streams, referencias a métodos ni operadores ternarios en `src`. Los listados utilizan ciclos `for` y las decisiones se escriben con `if/else`.
La aplicación no carga datos de prueba al arrancar. Se retiró `DataInitializer`, `CommandLineRunner` y el método con `String...`. Los datos existentes en PostgreSQL se conservan; los nuevos se ingresan por Swagger o pgAdmin. Las pruebas preparan sus propios datos exclusivamente en H2.
Spring Security conserva los componentes del demo. Su configuración usa implementaciones de `Customizer` porque la versión del framework recibe esa interfaz y se solicitó evitar lambdas. El demo utiliza lambdas en este punto; la variante sin lambdas es una adaptación del proyecto, no una sintaxis cuya enseñanza en clase se haya confirmado.

Se retiraron la subida y descarga de archivos físicos y el borrado automático de los datos asociados a un reporte: esas implementaciones no aparecen en los demos revisados. Adjunto conserva su CRUD de nombre, URL, tipo y fecha; guardar una URL no copia un archivo al servidor. Los archivos que ya existan en disco no se eliminan con este cambio.

La eliminación de reportes ahora hace un solo `delete`, igual que los CRUD del ejemplo. Si tiene comentarios, adjuntos o una recomendación, la FK impide borrarlo y la API responde `409`. Primero se eliminan esos registros mediante sus CRUD y luego el reporte. No se usa `@Transactional` en el código de la aplicación; en una prueba aislada de H2 se utiliza para revertir sus datos al terminar.

Las anotaciones de Swagger que describen las consultas se conservaron por el requisito de documentarlas. Son documentación añadida al proyecto: no hay evidencia de que cada anotación aparezca en los demos. Comparar el código confirma sus componentes y diferencias, pero no permite asegurar que la profesora haya enseñado cada variante de sintaxis.

## Por qué crear devuelve 201

En `demoSI/demoSI/src/main/java/pe/edu/upc/demosi/controllers/CropController.java`, línea 60, se utiliza `ResponseEntity.created(location).body(responseDTO)`. Esa respuesta tiene estado HTTP **201 Created**: se creó un registro. También incluye la cabecera `Location` con su dirección.

En `ComentarioController` se separan los pasos para leerlos con claridad:

```java
Comentario guardado = service.registrar(datos);
ComentarioDTO respuesta = convertir(guardado);
return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
```

El servicio guarda el comentario, `convertir` prepara el DTO de salida y `ResponseEntity` devuelve el DTO con estado 201. `HttpStatus.CREATED` es el nombre de ese mismo número. No es el ID del comentario ni un dato de PostgreSQL. La respuesta de comentario no incluye `Location`.

Hay ocho controladores de entidades, más `LoginController` para autenticar: nueve en total. `POST /registro` está en `UsuarioController`. Se retiraron `RegistroController`, `InicioController` y el controlador auxiliar de administrador. Swagger se abre directamente en `/swagger-ui/index.html`; la raíz `/` no es una pantalla del sistema.

## Por qué existen estos DTO

La retroalimentación revisada no establece una cantidad máxima de DTO. El ejemplo demoSI también separa `CropDTOInsert` y `CropDTOList`: recibir datos y devolverlos pueden necesitar campos distintos.

La carpeta `dtos` contiene 16 clases, todas utilizadas, después de retirar cuatro DTO correspondientes a consultas eliminadas:

| Uso | Cantidad | Motivo |
|---|---|---|
| CRUD de categoría, reporte, usuario, comentario, adjunto y recomendación | 9 | Seleccionar campos y validar entradas. Usuario mantiene una salida sin contraseña y una entrada distinta para crear o actualizar |
| Login | 2 | La entrada recibe correo y contraseña; la salida entrega token y datos de sesión |
| Registro público | 2 | La entrada no permite escoger rol ni estado; la salida confirma la cuenta sin devolver contraseña |
| Estadísticas | 2 | Cada consulta agrupada devuelve columnas diferentes; son resultados de consultas, no nuevas tablas |
| ErrorResponse | 1 | Mantiene una respuesta de error consistente con estado, mensaje y ruta |

Se conservan los DTO que usa cada contrato; solo se eliminaron los cuatro que dejaron de tener una consulta asociada. Con la reorganización de Yair, la carpeta se llama `configs` y solo contiene `ModelMapperConfig`; la seguridad sigue en `securities`. Las interfaces usan prefijo `I` y las implementaciones se encuentran en `servicesimplements`.

## Correcciones funcionales

- `GET /api/users`: lista todos sin parámetros y sin contraseña. Se retiró el filtro por estado.
- `GET /api/recomendaciones`: lista todas sin parámetros; prioridad es una operación separada.
- La lógica de usuarios, roles, ubicaciones, comentarios, adjuntos y recomendaciones pasa a servicios.
- El autor del reporte proviene del token y se conserva al actualizar. Un usuario no puede asignarse técnicos ni editar reportes ajenos.
- Una incidencia nueva inicia en `ABIERTO`; se registran fechas de asignación y resolución.
- Eliminar una incidencia con datos asociados responde `409` hasta borrarlos mediante sus CRUD; después responde `204`.
- Login valida correo y contraseña; registro conserva el mínimo de seis caracteres y BCrypt.
- Las consultas agrupadas usan SQL explícito y conversión por columnas. Se corrigió la numeración repetida de consultas.

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

## Probar en Swagger local

1. Tener PostgreSQL activo en `localhost:5433`, base `fixcampus`.
2. Ejecutar `FixcampusApplication` en IntelliJ o `mvnw.cmd spring-boot:run` desde la carpeta que contiene `pom.xml`.
3. Abrir `http://localhost:8080/swagger-ui/index.html`.
4. Usar `POST /login` con una cuenta administradora existente. En la base local comprobada: `{"correo":"admin@fixcampus.com","password":"admin123"}`. El arranque no crea esta cuenta.
5. Copiar el token en **Authorize** y probar los ejemplos de las diez consultas. Una lista vacía es válida si no hay coincidencias.
6. Para crear incidencias, obtener primero IDs reales de categorías y ubicaciones mediante sus GET; no asumir que existe el ID 1.

## JWT HS512 y clave

Se siguió `demoSI_seguridad`: la clave utiliza `HmacSHA512`; el token se firma con `MacAlgorithm.HS512` y el decodificador acepta HS512. `JwtConfig` comprueba que la clave tenga al menos **64 bytes** al convertirla a UTF-8. Una clave corta detiene el arranque con un mensaje claro.

`application.properties` contiene una clave local de prueba de 64 caracteres ASCII (64 bytes). `JWT_SECRET`, si está definida, reemplaza ese valor. En Render se configura una clave privada de al menos 64 bytes en **Environment → JWT_SECRET**; no se deben copiar las claves de prueba. Esta modificación local no cambia la variable ni despliega Render.

512 bits equivalen a 64 bytes; contar caracteres solo asegura esa longitud si son ASCII. El algoritmo firma el token, no cifra su contenido. Las contraseñas siguen usando BCrypt. Los tokens HS256 anteriores dejan de ser válidos: iniciar sesión otra vez después de reiniciar el backend.

## Base de datos nueva, sin carga automática

El arranque crea las tablas mediante `ddl-auto=update`, pero no crea registros. Si la base está vacía:

1. En pgAdmin, abrir Query Tool de la base `fixcampus` y consultar `SELECT * FROM rol;`.
2. Solo si faltan ambos roles, ejecutar:

```sql
INSERT INTO rol (nombre, nivel_acceso, descripcion)
VALUES ('ADMIN', 'TOTAL', 'Administrador del sistema'),
       ('USUARIO', 'BASICO', 'Usuario que reporta incidencias');
```

3. En Swagger, usar `POST /registro` con nombre, apellido, correo y contraseña de seis caracteres o más. El backend genera BCrypt; no ingresar la contraseña directamente en la tabla.
4. Para establecer la primera cuenta administradora, consultar `SELECT id_rol, nombre FROM rol;` y `SELECT id_usuario, correo FROM usuario;`. Usar los IDs reales en `UPDATE usuario SET id_rol = ID_ADMIN WHERE id_usuario = ID_USUARIO;`. Reemplazar ambos marcadores por los números consultados. Después iniciar sesión de nuevo para obtener un token ADMIN.
5. Con ese token crear categorías y ubicaciones mediante sus POST en Swagger. Luego crear reportes usando los IDs obtenidos.

Estos pasos se ejecutan manualmente una vez. El código no promueve usuarios ni crea cuentas al iniciar.

## Qué explicar a la profesora

- **CRUD:** crear con POST, leer con GET, actualizar con PUT y eliminar con DELETE. El ID se utiliza para buscar, actualizar o borrar un registro; no para listar todos.
- **DTO:** selecciona qué datos recibe/devuelve la API. `RegistroRequestDTO` recibe la contraseña; `UsuarioDTO` la excluye. Los DTO de estadísticas contienen las columnas del resultado, no una tabla nueva.
- **API:** FixCampus es una API REST desarrollada por el equipo. Render aloja esa API; PostgreSQL guarda sus datos. Swagger la documenta y permite probarla. Esto no equivale a consumir una API externa de terceros.
- **Autenticación:** `AuthenticationManager` valida las credenciales con el usuario cargado por `JwtUserDetailsService` y con BCrypt.
- **Autorización:** `@PreAuthorize` comprueba el rol antes de ejecutar una operación. Un token de USUARIO produce 403 al listar usuarios.
- **Ruta pública:** `permitAll()` en `SecurityConfig` permite `/login` y `/registro` sin token. La ubicación del método en UsuarioController no libera la ruta por sí sola.
- **BCrypt:** es un hash de contraseña, no un cifrado reversible ni una función de PostgreSQL. La base almacena el resultado que genera Java.
- **JWT:** firma y fecha de expiración permiten validar el token. Su contenido no está cifrado y no debe incluir contraseñas.
- **401 / 403 / 404:** falta autenticación válida / falta permiso / no existe el recurso.
- **Variables:** `application.properties` lee DB_URL, DB_USERNAME y DB_PASSWORD. En Render esos valores se configuran en Environment; la URL JDBC no está guardada dentro de una tabla.
- **Tablas independientes:** Rol, Categoria y Ubicacion no tienen FK a otra tabla. Usuario, Reporte, Comentario, Adjunto y Recomendacion sí dependen de relaciones.
- **JOIN vs GROUP BY:** JOIN relaciona tablas; GROUP BY forma grupos; COUNT cuenta elementos del grupo.

## Comprobación

- Compilación y empaquetado mediante Maven.
- Cinco pruebas automatizadas que cubren arranque, Swagger, registro/login, CRUD de ocho entidades, permisos, consultas y eliminación con relaciones.
- Pruebas HTTP adicionales contra PostgreSQL local con datos temporales y limpieza posterior.
- CRUD de metadatos de adjuntos; FK que impide borrar un reporte en uso y eliminación posterior en el orden correcto.
- Revisión de fuentes sin lambdas/streams/referencias a métodos y sin configuración Angular/Firebase.

Los cambios corresponden al proyecto local. Las comprobaciones locales no garantizan que Render ya ejecute esta versión: se requiere publicar y desplegar los cambios para eso.
