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
| SQL nativo + `List<Object[]>` | Cinco agrupaciones convertidas a DTO mediante `for`; se evita usar streams o lambdas para recorrer sus resultados |
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
ComentarioDTOList respuesta = convertir(guardado);
return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
```

El servicio guarda el comentario, `convertir` prepara el DTO de salida y `ResponseEntity` devuelve el DTO con estado 201. `HttpStatus.CREATED` es el nombre de ese mismo número. No es el ID del comentario ni un dato de PostgreSQL. La respuesta de comentario no incluye `Location`.

Hay ocho controladores de entidades, más `LoginController` para autenticar: nueve en total. `POST /registro` está en `UsuarioController`. Se retiraron `RegistroController`, `InicioController` y el controlador auxiliar de administrador. Swagger se abre directamente en `/swagger-ui/index.html`; la raíz `/` no es una pantalla del sistema.

## Por qué existen estos DTO

La retroalimentación revisada no establece una cantidad máxima de DTO. El ejemplo demoSI también separa `CropDTOInsert` y `CropDTOList`: recibir datos y devolverlos pueden necesitar campos distintos.

La carpeta `dtos` contiene 26 clases utilizadas. El refactor de Yair separa entradas y salidas, como el ejemplo CropDTOInsert/CropDTOList: 16 clases para los ocho CRUD, dos para login, dos para registro, cinco para resultados agrupados y una para errores. Los DTO de salida excluyen contraseña, permiten devolver IDs/fechas y evitan exponer entidades relacionadas completas.

Con la reorganización de Yair, la carpeta se llama `configs` y solo contiene `ModelMapperConfig`; la seguridad sigue en `securities`. Las interfaces usan prefijo `I` y las implementaciones se encuentran en `servicesimplements`.

## Correcciones funcionales

- `GET /api/users`: lista todos sin parámetros y sin contraseña. Se retiró el filtro por estado.
- `GET /api/recomendaciones`: lista todas sin parámetros; prioridad es una operación separada.
- La lógica de usuarios, roles, ubicaciones, comentarios, adjuntos y recomendaciones pasa a servicios.
- El autor del reporte proviene del token y se conserva al actualizar. Un usuario no puede asignarse técnicos ni editar reportes ajenos.
- Una incidencia nueva inicia en `ABIERTO`; se registran fechas de asignación y resolución.
- Eliminar una incidencia con datos asociados responde `409` hasta borrarlos mediante sus CRUD; después responde `204`.
- Login valida correo y contraseña; registro conserva el mínimo de seis caracteres y BCrypt.
- Las consultas agrupadas usan SQL explícito y conversión por columnas. Se corrigió la numeración repetida de consultas.

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

Cada consulta está comentada en su repositorio y tiene una descripción breve en Swagger. Los resultados `Object[]` de las cinco agrupaciones se convierten a DTO en los servicios con ciclos `for`; cada posición coincide con el orden del SELECT. No se utilizan lambdas ni streams.

### Métodos auxiliares

Login, registro, CRUD, consulta de reportes propios, adjuntos de un reporte y conteo de usuarios siguen disponibles. Son operaciones necesarias para seguridad y CRUD; no se añaden al catálogo de diez consultas académicas. `GET /api/users` lista todos, sin parámetros y sin contraseña.

Se retiraron los filtros anteriores por campus de ubicación, reportes por campus sin agregación, recomendaciones por categoría con dos JOIN y recomendación por reporte. Sus ocho CRUD continúan funcionando; para leer una recomendación concreta se usa `GET /api/recomendaciones/{id}`.

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
- **DTO:** selecciona qué datos recibe/devuelve la API. `RegistroRequestDTO` recibe la contraseña; `UsuarioDTOList` la excluye. Los DTO de estadísticas contienen las columnas del resultado, no una tabla nueva.
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

La integración posterior de `yair-refactor` hasta `9445874` y sus correcciones se describen en [Actualización de consultas](Actualizacion-consultas-y-documento-2026-10-01.md). La verificación actual comprobó 54 operaciones, 121 peticiones HTTP y 83 archivos Java sin las construcciones restringidas.
