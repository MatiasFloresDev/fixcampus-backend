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
| SQL nativo + `List<Object[]>` | Cinco agrupaciones convertidas a DTO mediante `for`, igual que las consultas de Attention en el demo |
| BCrypt y JWT | Registro, login y acceso mediante Bearer token |
| `@PreAuthorize` | Permiso ADMIN/USUARIO por operación |
| OpenAPI | Swagger con descripciones de consultas y rutas públicas sin candado |

El recorrido es `Controller → Service (interfaz) → ServiceImpl → Repository → PostgreSQL`.
El controlador atiende HTTP y convierte la respuesta; el servicio aplica las reglas; el repositorio consulta o guarda; la entidad representa la tabla.

No se usan lambdas, streams, referencias a métodos ni operadores ternarios en `src`. Los listados utilizan ciclos `for` y las decisiones se escriben con `if/else`.
La aplicación no carga datos de prueba al arrancar. Se retiró `DataInitializer`, `CommandLineRunner` y el método con `String...`. Los datos existentes en PostgreSQL se conservan; los nuevos se ingresan por Swagger o pgAdmin. Las pruebas preparan sus propios datos exclusivamente en H2.
Spring Security conserva los componentes del demo. Su configuración usa implementaciones de `Customizer` porque la versión del framework exige esa interfaz y se solicitó evitar lambdas. No se eliminó la seguridad para simplificar la sintaxis.

`@Transactional` en la eliminación de reportes agrupa los cambios de la base: si falla una eliminación, se revierten las demás. Es necesario porque primero se eliminan comentarios, recomendación y adjuntos, y luego el reporte. Los archivos se limpian después de comprobar las restricciones de la base.

Hay ocho controladores de entidades, más `LoginController` para autenticar: nueve en total. `POST /registro` está en `UsuarioController`. Se retiraron `RegistroController`, `InicioController` y el controlador auxiliar de administrador. Swagger se abre directamente en `/swagger-ui/index.html`; la raíz `/` no es una pantalla del sistema.

## Por qué existen estos DTO

La retroalimentación revisada no establece una cantidad máxima de DTO. El ejemplo demoSI también separa `CropDTOInsert` y `CropDTOList`: recibir datos y devolverlos pueden necesitar campos distintos.

La carpeta `dtos` contiene 19 clases, todas utilizadas:

| Uso | Cantidad | Motivo |
|---|---|---|
| CRUD de categoría, reporte, usuario, comentario, adjunto y recomendación | 9 | Seleccionar campos y validar entradas. Usuario mantiene una salida sin contraseña y una entrada distinta para crear o actualizar |
| Login | 2 | La entrada recibe correo y contraseña; la salida entrega token y datos de sesión |
| Registro público | 2 | La entrada no permite escoger rol ni estado; la salida confirma la cuenta sin devolver contraseña |
| Estadísticas | 5 | Cada consulta agrupada devuelve columnas diferentes; son resultados de consultas, no nuevas tablas |
| ErrorResponse | 1 | Mantiene una respuesta de error consistente con estado, mensaje y ruta |

No se eliminan estas clases solo para reducir el número: hacerlo obligaría a mezclar contratos o devolver campos que no corresponden. En `config` solo queda `ModelMapperConfig`; la seguridad sigue en `securities`.

## Correcciones funcionales

- `GET /api/users`: lista todos sin parámetros; el filtro se usa en `/api/users/estado?estado=ACTIVO`.
- `GET /api/recomendaciones`: lista todas sin parámetros; prioridad es una operación separada.
- La lógica de usuarios, roles, ubicaciones, comentarios, adjuntos y recomendaciones pasa a servicios.
- El autor del reporte proviene del token y se conserva al actualizar. Un usuario no puede asignarse técnicos ni editar reportes ajenos.
- Una incidencia nueva inicia en `ABIERTO`; se registran fechas de asignación y resolución.
- Eliminar una incidencia con datos hijos respeta las FK.
- Login valida correo y contraseña; registro conserva el mínimo de seis caracteres y BCrypt.
- Las consultas agrupadas usan SQL explícito y conversión por columnas. Se corrigió la numeración repetida de consultas.

## Diez consultas para el requisito de cinco integrantes

La profesora pide una consulta simple y otra con JOIN y agregación por integrante. Esta selección cumple los tipos solicitados; el equipo debe asignar los responsables reales en Trello y conservar sus evidencias de participación.

| Par | Consulta simple | Consulta con JOIN y COUNT |
|---|---|---|
| 1 | Q1: categorías por nombre | Q18: incidencias por categoría entre fechas |
| 2 | Q2: roles por nombre | Q10: incidencias por usuario, año y mes |
| 3 | Q3: ubicaciones por campus | Q11: incidencias por campus y estado |
| 4 | Q4: usuarios por estado | Q14: comentarios de un usuario por reporte |
| 5 | Q5: adjuntos por tipo | Q19: evidencias de un usuario |

Las búsquedas adicionales que ya estaban implementadas siguen disponibles. Q8, Q9, Q13 y Q17 tienen JOIN, pero **no sustituyen una consulta agrupada** porque no cuentan, suman ni calculan promedios.

## Probar en Swagger local

1. Tener PostgreSQL activo en `localhost:5433`, base `fixcampus`.
2. Ejecutar `FixcampusApplication` en IntelliJ o `mvnw.cmd spring-boot:run` desde la carpeta que contiene `pom.xml`.
3. Abrir `http://localhost:8080/swagger-ui/index.html`.
4. Ejecutar `POST /login` con una cuenta administradora existente. En la base local comprobada existe esta cuenta; ya no se crea automáticamente:

```json
{"correo":"admin@fixcampus.com","password":"admin123"}
```

5. Copiar `token` y pegarlo en **Authorize**. El esquema Bearer añade el prefijo automáticamente.
6. Consultar categorías y ubicaciones y usar sus IDs reales para crear una incidencia. No asumir que el ID 1 existe.
7. Probar estas consultas:

| Consulta | Ruta y ejemplo |
|---|---|
| Q1 | `/api/categories?nombre=Electricidad` |
| Q2 | `/api/roles?nombre=ADMIN` |
| Q3 | `/api/locations?campus=Monterrico` |
| Q4 | `/api/users/estado?estado=ACTIVO` |
| Q5 | `/api/attachments?tipoArchivo=pdf` |
| Q10 | `/api/reports/estadisticas/por-usuario-mes` |
| Q11 | `/api/reports/estadisticas/por-campus?estado=ABIERTO` |
| Q14 | `/api/comments/estadisticas/por-reporte?correo=usuario@fixcampus.com` |
| Q18 | `/api/reports/estadisticas/por-categoria?desde=2026-09-01&hasta=2026-10-01` |
| Q19 | `/api/attachments/estadisticas/por-usuario?correo=usuario@fixcampus.com` |

Una lista vacía es válida cuando no hay datos coincidentes. Q18 incluye la fecha `desde` y excluye `hasta`: sirve para tomar un mes completo sin incluir el primero del siguiente.

### Cómo explicar las dos nuevas consultas

**Q18:** “Uno reporte con categoría usando la FK. `WHERE` selecciona el periodo, `GROUP BY` reúne cada categoría y `COUNT` cuenta sus incidencias. El resultado permite identificar el tipo de problema más frecuente en ese periodo”.

Ejemplo: tres reportes de Electricidad y dos de Limpieza en septiembre generan dos filas, con cantidades 3 y 2. El DTO contiene `categoriaId`, `categoria` y `cantidad`.

**Q19:** “Uno reporte con su usuario y hago LEFT JOIN con los adjuntos. Filtro por correo, agrupo por usuario y cuento `id_adjunto`. Si tiene incidencias sin archivos, esos valores nulos no aumentan el contador”.

Ejemplo: un usuario tiene dos incidencias; la primera incluye dos archivos y la segunda ninguno. La consulta devuelve `cantidadEvidencias: 2`. Si aún no tiene incidencias, devuelve una lista vacía.

**Conversión:** el repositorio devuelve cada fila como `Object[]`. Las posiciones siguen el orden del `SELECT`. El servicio usa `Number.longValue()` o `intValue()` para cantidades e IDs, crea el DTO y lo añade a una lista con un `for`.

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
- Carga y descarga real de un PNG, rechazo de la cuarta evidencia y rechazo de descarga por otra cuenta.
- Revisión de fuentes sin lambdas/streams/referencias a métodos y sin configuración Angular/Firebase.

Los cambios corresponden al proyecto local. Las comprobaciones locales no garantizan que Render ya ejecute esta versión: se requiere publicar y desplegar los cambios para eso.
