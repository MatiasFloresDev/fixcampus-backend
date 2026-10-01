# FixCampus — Especificación funcional y técnica

## 1. Propósito

FixCampus centraliza incidencias de espacios del campus. El sistema captura el contexto mínimo para actuar —qué pasó, dónde y de qué tipo— y deja una consulta posterior para la persona que reportó.

## 2. Alcance actual

### Incluido y demostrable

- Registro de cuenta y autenticación con token.
- Catálogos de categorías y ubicaciones consultados por la API.
- Registro de incidencias con categoría, ubicación, detalle, descripción, prioridad y estado.
- Consulta de reportes propios.
- CRUD de entidades principales en la API protegido por roles.
- Documentación OpenAPI disponible en Swagger.
- CRUD de metadatos de evidencias: nombre del archivo, URL, tipo y fecha. Consulta por reporte con permiso de su propietario o administrador.
- Seis consultas agrupadas con JOIN y COUNT para analizar las incidencias.

### Fuera del alcance de la API actual

- Subida y descarga de archivos físicos.
- Bandeja visual para personal de atención.
- Notificaciones automáticas y cálculo programado de SLA.
- Línea de tiempo y conversación en la pantalla de detalle.

Estos puntos están documentados como historias de la siguiente iteración; no se presentan como terminados.

## 3. Reglas funcionales

| Código | Regla |
|---|---|
| RF-01 | Solo una cuenta activa puede crear un reporte. |
| RF-02 | El correo de usuario es único. |
| RF-03 | Un reporte requiere título, descripción, categoría y ubicación. |
| RF-04 | Todo reporte nuevo inicia en estado `ABIERTO`. |
| RF-05 | Un usuario consulta únicamente sus propios reportes desde “Mis reportes”. |
| RF-06 | Los catálogos se mantienen centralizados para evitar valores inconsistentes. |
| RF-07 | Las operaciones de administración requieren rol `ADMIN`. |
| RF-08 | Las contraseñas se guardan como hash y no forman parte de las respuestas. |

## 4. Roles y permisos

| Recurso | Comunidad | Administrador |
|---|---:|---:|
| Registrarse / iniciar sesión | Sí | Sí |
| Consultar categorías y ubicaciones | Sí | Sí |
| Crear y consultar mis reportes | Sí | Sí |
| CRUD de categorías | No | Sí |
| CRUD de ubicaciones | No | Sí |
| CRUD de usuarios, roles y reportes | No | Sí |
| Consultar indicadores | No | Sí |

## 5. API actual

Base local: `http://127.0.0.1:8080`  
Swagger: `http://localhost:8080/swagger-ui/index.html`  
OpenAPI: `http://localhost:8080/v3/api-docs`

| Módulo | Endpoints principales | Permiso |
|---|---|---|
| Registro | `POST /registro` | Público |
| Login | `POST /login` | Público |
| Reportes propios | `GET /api/reports/mis-reportes` | `ADMIN` o `USUARIO` |
| Reportes | `GET/POST/PUT/DELETE /api/reports` | Según operación |
| Categorías | `GET/POST/PUT/DELETE /api/categories` | Consulta autenticada / cambios admin |
| Ubicaciones | `GET/POST/PUT/DELETE /api/locations` | Consulta autenticada / cambios admin |
| Usuarios | `GET/POST/PUT/DELETE /api/users` | Admin |
| Roles | `GET/POST/PUT/DELETE /api/roles` | Admin |
| Adjuntos | `GET/POST/PUT/DELETE /api/attachments`; `GET /api/attachments/reporte/{reporteId}` | CRUD admin; consulta por reporte propio o admin |
| Comentarios | `GET/POST/PUT/DELETE /api/comments` | Admin |
| Recomendaciones | `GET/POST/PUT/DELETE /api/recomendaciones` | Admin |
| Indicadores | Incidencias por usuario/mes, campus y categoría entre fechas; total por categoría incluyendo cero; comentarios por reporte; evidencias por usuario | Admin |

## 6. Contrato de creación de reporte

### Solicitud

```json
{
  "categoriaId": 10,
  "ubicacionId": 7,
  "titulo": "WiFi no disponible",
  "descripcion": "No hay conexión a la red en la biblioteca.",
  "detalleUbicacion": "Sala de lectura",
  "prioridad": "MEDIA",
  "estado": "ABIERTO"
}
```

El cliente envía el token en `Authorization: Bearer <token>`. El backend obtiene el usuario autenticado y guarda la relación, por lo que el usuario no debe poder hacerse pasar por otra cuenta.

### Respuestas esperadas

- `201 Created`: reporte guardado con `idReporte`.
- `400 Bad Request`: datos obligatorios inválidos.
- `401 Unauthorized`: token ausente o vencido.
- `403 Forbidden`: rol sin permiso.
- `404 Not Found`: categoría o ubicación inexistente.

## 7. Modelo de datos

```mermaid
erDiagram
  ROL ||--o{ USUARIO : asigna
  USUARIO ||--o{ REPORTE : reporta
  USUARIO ||--o{ REPORTE : atiende
  CATEGORIA ||--o{ REPORTE : clasifica
  UBICACION ||--o{ REPORTE : localiza
  REPORTE ||--o{ ADJUNTO : contiene
  REPORTE ||--o{ COMENTARIO : recibe
  USUARIO ||--o{ COMENTARIO : escribe
  REPORTE ||--o| RECOMENDACION : tiene
```

La relación de `REPORTE` con `CATEGORIA` y `UBICACION` evita guardar texto libre repetido. `ADJUNTO`, `COMENTARIO` y `RECOMENDACION` tienen CRUD en la API. Una incidencia puede tener varios adjuntos y comentarios, y como máximo una recomendación de mantenimiento.

La FK impide borrar un reporte que todavía tenga adjuntos, comentarios o una recomendación. La API responde `409`; después de eliminar esos registros mediante sus CRUD se puede eliminar el reporte con respuesta `204`.

## 8. Arquitectura

```text
Cliente HTTP o Swagger
        │ HTTP + Bearer token
        ▼
Spring Boot 4 / Spring Security / Spring Data JPA
        │ JDBC
        ▼
PostgreSQL (base fixcampus)
```

### Capas del backend

- `controllers`: rutas HTTP y códigos de respuesta.
- `servicesinterfaces`: contratos de negocio.
- `servicesimplements`: reglas, asociaciones y consultas.
- `repositories`: acceso a PostgreSQL mediante JPA.
- `entities`: entidades persistentes.
- `dtos`: contratos de entrada y salida.
- `config`: configuración de ModelMapper.
- `securities`: BCrypt, JWT, reglas de acceso y configuración de Swagger.

### Decisiones técnicas

1. El cliente obtiene catálogos por API para que los cambios del administrador estén disponibles sin modificar el backend.
2. El backend valida el rol y el token; ocultar una opción del cliente no se considera seguridad.
3. El arranque no crea datos de prueba. Roles y primera cuenta administradora se preparan manualmente en una base nueva; después los catálogos se gestionan por sus CRUD.
4. La base local usa PostgreSQL en el puerto `5433`; las pruebas automáticas usan H2 aislado.
5. Swagger sirve para revisar contratos y probar operaciones administrativas sin inventar una capacidad que todavía no existe.

## 9. Requisitos no funcionales

| Código | Requisito | Verificación |
|---|---|---|
| RNF-01 | La contraseña no aparece en logs ni respuestas. | Revisión de DTO y prueba de registro. |
| RNF-02 | Las operaciones privadas requieren autenticación. | Petición sin token debe devolver `401`. |
| RNF-03 | La API debe responder errores con un mensaje claro. | Repetir una petición con datos inválidos. |
| RNF-04 | Los endpoints deben estar documentados y ser comprobables. | Revisar Swagger y ejecutar una petición. |
| RNF-05 | El backend debe responder errores con un código claro. | Pruebas `400`, `401`, `403` y `404`. |
| RNF-06 | La aplicación debe poder levantar en otra máquina con variables de entorno. | Cambiar `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y `PORT`. |

## 10. Matriz de pruebas para la entrevista

| Prueba | Paso | Resultado esperado |
|---|---|---|
| P-01 | Registrar cuenta nueva | `201` y mensaje de confirmación |
| P-02 | Repetir correo | Error sin crear una segunda cuenta |
| P-03 | `POST /login` con credenciales válidas | `200` y token JWT |
| P-04 | `POST /login` con contraseña incorrecta | `401` |
| P-05 | Consultar categorías y ubicaciones con token | `200` y catálogos disponibles |
| P-06 | `POST /api/reports` sin título | `400` por validación del DTO |
| P-07 | Enviar reporte válido | `201` y aparece en Mis reportes |
| P-08 | Consultar sin token | `401` |
| P-09 | CRUD de catálogo como usuario | `403` |
| P-10 | CRUD de catálogo como admin | `201`, `200` o `204` según operación |

## 11. Evidencias que conviene llevar

- Captura de Swagger con la consulta de categorías y ubicaciones.
- Captura de Swagger con `POST /api/reports` y respuesta `201`.
- Captura del caso de error de validación.
- Historial de cambios de este documento o commit asociado.
- Diagrama de datos y matriz de trazabilidad.
