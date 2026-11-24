# FixCampus — Historias de Usuario

## Alcance

FixCampus registra incidencias de los espacios del campus y permite seguir su atención. El backlog está separado por estado para que la documentación no prometa como terminada una función que todavía está en diseño.

- **Implementada:** se puede demostrar hoy en la web o en Swagger.
- **Parcial:** existe en el backend o en el modelo, pero falta cerrar el flujo en la interfaz.
- **Propuesta:** siguiente iteración, con criterios listos para desarrollar.

## Roles

- **Comunidad:** estudiante o trabajador que reporta un problema.
- **Personal de atención:** usuario que recibe, comenta y actualiza incidencias.
- **Administrador:** mantiene catálogos, usuarios y reglas de operación.

## Definition of Ready y Definition of Done

Una historia entra al sprint cuando tiene actor, objetivo, beneficio, reglas, dependencias y una forma concreta de probarla. Se considera terminada cuando los criterios pasan en la interfaz o en la API, se actualiza la documentación y queda una evidencia fechada.

## Backlog priorizado

### HU-01 — Crear una cuenta con validación

**Como** integrante de la comunidad, **quiero** registrarme con mis datos y un correo válido, **para** poder reportar incidencias y consultar su seguimiento.

- **Prioridad:** Must — **Estado:** Implementada — **Épica:** Acceso.
- **Reglas:** nombre y apellido obligatorios; contraseña mínima de seis caracteres; el correo no puede repetirse; la contraseña nunca se devuelve en una respuesta.
- **Criterios de aceptación:**
  1. Si completo todos los datos válidos, el sistema crea una cuenta y devuelve confirmación sin exponer la contraseña.
  2. Si el correo ya existe, la cuenta no se duplica y recibo un mensaje entendible.
  3. Si falta un dato o la contraseña no cumple la longitud mínima, el formulario no envía la solicitud.
  4. Si la API no está disponible, el formulario abandona el estado de carga y muestra que debe intentarse nuevamente.
- **Evidencia:** pantalla `/registrarse`, `POST /registro`, respuesta `201` y prueba de correo duplicado.

### HU-02 — Iniciar sesión y mantener una sesión segura

**Como** usuario registrado, **quiero** iniciar sesión y cerrar sesión, **para** acceder solo a mis incidencias.

- **Prioridad:** Must — **Estado:** Implementada — **Épica:** Acceso.
- **Reglas:** una credencial incorrecta no revela si falló el correo o la contraseña; una sesión vencida devuelve al acceso; cerrar sesión elimina el token local.
- **Criterios de aceptación:**
  1. Con credenciales válidas, se guarda la sesión y se abre `/reportar`.
  2. Con credenciales inválidas, la pantalla permanece en acceso y muestra un mensaje sin detalles sensibles.
  3. Al cerrar sesión, las pantallas privadas dejan de estar disponibles.
  4. Una petición privada sin token responde `401`.
- **Evidencia:** `POST /login`, navegación a `/reportar`, botón “Cerrar sesión” y prueba de `401`.

### HU-03 — Registrar una incidencia con contexto suficiente

**Como** integrante de la comunidad, **quiero** describir un problema indicando categoría, ubicación y detalle del lugar, **para** que el equipo pueda encontrarlo y atenderlo sin pedirme la información básica otra vez.

- **Prioridad:** Must — **Estado:** Implementada — **Épica:** Reportes.
- **Reglas:** título, descripción, categoría y ubicación son obligatorios; el reporte inicia en estado `ABIERTO`; se asocia al usuario autenticado.
- **Criterios de aceptación:**
  1. El formulario carga categorías y ubicaciones desde la API, no desde valores escritos a mano.
  2. Si falta un campo obligatorio, no se crea el reporte y se indica qué falta.
  3. Con datos válidos, la API devuelve `201` y un identificador.
  4. El mensaje de éxito solo aparece después de recibir una respuesta exitosa.
  5. Si falla el servidor, los datos no se presentan como guardados y el botón vuelve a estar disponible.
- **Evidencia:** formulario `/reportar`, `GET /api/categories`, `GET /api/locations` y `POST /api/reports`.

### HU-04 — Consultar mis reportes y su estado

**Como** usuario que reportó una incidencia, **quiero** ver mis reportes ordenados por los más recientes, **para** saber qué se registró y qué sigue pendiente.

- **Prioridad:** Must — **Estado:** Implementada — **Épica:** Seguimiento.
- **Reglas:** un usuario solo consulta sus propios reportes; si no hay resultados se muestra un estado vacío; cada tarjeta muestra estado, categoría, descripción y ubicación.
- **Criterios de aceptación:**
  1. Al entrar a `/reportar`, la lista se obtiene con `GET /api/reports/mis-reportes`.
  2. Un reporte recién creado aparece sin recargar manualmente el navegador.
  3. Los reportes se muestran del más nuevo al más antiguo.
  4. Una cuenta sin reportes ve una explicación y una acción para registrar el primero.
- **Evidencia:** estado vacío, lista con un reporte y consulta autenticada en Swagger.

### HU-05 — Mantener catálogos válidos

**Como** administrador, **quiero** crear, editar, consultar y retirar categorías y ubicaciones, **para** evitar que el formulario acumule nombres distintos para el mismo problema.

- **Prioridad:** Must — **Estado:** Implementada en API / Parcial en interfaz — **Épica:** Configuración.
- **Reglas:** los nombres de categoría son únicos; una ubicación conserva campus, edificio, piso, zona y tipo; solo un administrador modifica catálogos.
- **Criterios de aceptación:**
  1. `GET` devuelve los catálogos disponibles para el formulario.
  2. Un administrador puede crear, actualizar y eliminar una categoría o ubicación desde Swagger.
  3. Un usuario común puede consultar los catálogos, pero recibe `403` al intentar modificarlos.
  4. El formulario refleja un catálogo actualizado al volver a cargar la pantalla.
- **Evidencia:** `/api/categories`, `/api/locations` y sus operaciones CRUD en Swagger.

### HU-06 — Adjuntar evidencia sin perder privacidad

**Como** usuario, **quiero** adjuntar una foto o archivo pequeño al reporte, **para** que el equipo pueda confirmar el problema sin una visita inicial.

- **Prioridad:** Should — **Estado:** Propuesta — **Épica:** Reportes.
- **Reglas:** formatos permitidos `jpg`, `png` y `pdf`; máximo 5 MB por archivo y 3 archivos por reporte; el enlace no debe ser público sin autorización.
- **Criterios de aceptación:**
  1. El sistema rechaza una extensión no permitida antes de subirla.
  2. Si el archivo supera el límite, explica el límite y conserva el resto del formulario.
  3. Un archivo aceptado queda asociado al `idReporte` y puede visualizarse con permiso.
  4. Al eliminar el reporte, sus adjuntos quedan eliminados o marcados para limpieza.
- **Dependencias:** entidad `Adjunto`, almacenamiento y permisos de descarga.

### HU-07 — Detectar reportes posiblemente duplicados

**Como** usuario que está por enviar una incidencia, **quiero** recibir una alerta si ya existe un reporte parecido en la misma zona, **para** no generar trabajo duplicado y poder seguir el caso existente.

- **Prioridad:** Should — **Estado:** Propuesta — **Épica:** Calidad de datos.
- **Reglas:** comparar categoría, ubicación y palabras relevantes del título durante una ventana configurable de 72 horas; nunca bloquear por completo sin permitir justificar un nuevo reporte.
- **Criterios de aceptación:**
  1. Si hay coincidencias, se muestran título, estado y fecha del reporte existente.
  2. El usuario puede abrir el reporte existente o confirmar “reportar de todas formas”.
  3. La alerta no expone reportes de otro campus o datos personales.
  4. La decisión del usuario queda registrada para medir falsos positivos.
- **Dependencias:** búsqueda por categoría/ubicación, regla de similitud y auditoría.

### HU-08 — Priorizar y clasificar una incidencia

**Como** personal de atención, **quiero** asignar prioridad y tipo de impacto a un reporte, **para** atender primero los problemas que afectan la seguridad o muchas clases.

- **Prioridad:** Must — **Estado:** Parcial — **Épica:** Operación.
- **Reglas:** prioridad `BAJA`, `MEDIA`, `ALTA` o `CRÍTICA`; una incidencia de seguridad o servicio general requiere justificación; los cambios quedan en historial.
- **Criterios de aceptación:**
  1. El personal puede cambiar prioridad sin modificar la descripción original.
  2. Una prioridad crítica exige seleccionar un motivo.
  3. El usuario reportante ve la prioridad publicada, pero no puede editarla.
  4. El tablero ordena por prioridad y antigüedad.
- **Evidencia actual:** campo `prioridad` en `Reporte` y operaciones CRUD; falta cerrar el tablero en la interfaz.

### HU-09 — Asignar responsable y controlar el SLA

**Como** coordinador, **quiero** asignar un reporte a un responsable con una fecha límite, **para** saber quién debe actuar y detectar retrasos.

- **Prioridad:** Must — **Estado:** Propuesta — **Épica:** Operación.
- **Reglas:** solo usuarios activos con rol de atención pueden ser asignados; la fecha límite depende de prioridad; un cambio de responsable exige motivo.
- **Criterios de aceptación:**
  1. Una asignación registra responsable, fecha y usuario que la realizó.
  2. El responsable ve una bandeja con pendientes, vencidos y próximos a vencer.
  3. El sistema marca “vencido” sin alterar el estado de atención.
  4. Un cambio de prioridad recalcula el SLA y conserva la fecha anterior en historial.
- **Dependencias:** roles de atención, `idTecnicoAsignado`, fechas de asignación/resolución y tarea programada.

### HU-10 — Conversar dentro del reporte

**Como** usuario o personal de atención, **quiero** agregar comentarios al reporte, **para** aclarar datos y dejar la conversación en un solo lugar.

- **Prioridad:** Should — **Estado:** Parcial en modelo — **Épica:** Comunicación.
- **Reglas:** cada comentario tiene autor y fecha; el usuario no puede borrar comentarios de otra persona; comentarios internos solo son visibles para el personal.
- **Criterios de aceptación:**
  1. Un comentario aparece ordenado cronológicamente después de guardarse.
  2. Se distinguen mensajes públicos de notas internas.
  3. El historial muestra quién hizo cada cambio.
  4. Un usuario sin relación con el reporte recibe `403`.
- **Evidencia actual:** entidad `Comentario` y controlador disponible; falta la vista de detalle.

### HU-11 — Recibir una línea de tiempo de atención

**Como** reportante, **quiero** ver los cambios de estado en una línea de tiempo, **para** entender qué ocurrió con mi solicitud sin preguntar por mensaje.

- **Prioridad:** Should — **Estado:** Propuesta — **Épica:** Seguimiento.
- **Reglas:** estados `ABIERTO`, `EN_REVISION`, `ASIGNADO`, `EN_ATENCION`, `RESUELTO` y `CERRADO`; cada transición registra fecha, actor y comentario opcional.
- **Criterios de aceptación:**
  1. El detalle muestra el estado actual y todas las transiciones en orden.
  2. No se puede pasar a `RESUELTO` sin una nota de solución.
  3. El reportante puede reabrir dentro de 7 días indicando el motivo.
  4. El sistema evita transiciones inválidas y devuelve un mensaje claro.

### HU-12 — Consultar indicadores sin exponer datos personales

**Como** administrador, **quiero** consultar incidencias por mes, campus, estado y categoría, **para** decidir dónde asignar mantenimiento.

- **Prioridad:** Should — **Estado:** Parcial en API — **Épica:** Reportes de gestión.
- **Reglas:** los indicadores usan datos agregados; no muestran correos ni contraseñas; el filtro por fecha debe indicar la zona horaria.
- **Criterios de aceptación:**
  1. El administrador filtra un rango y obtiene totales consistentes con la lista de reportes.
  2. El tablero diferencia abiertas, vencidas, resueltas y reabiertas.
  3. Un rango sin datos muestra cero y no rompe el gráfico.
  4. Un usuario común no puede consultar indicadores administrativos.
- **Evidencia actual:** consultas de dashboard en el backend; falta cerrar la pantalla visual.

## Trazabilidad rápida

| Historia | Pantalla actual | API / dato | Prueba sugerida |
|---|---|---|---|
| HU-01 | `/registrarse` | `POST /registro` | Registro válido y correo duplicado |
| HU-02 | `/iniciar-sesion` | `POST /login` | Login válido, inválido y cierre |
| HU-03 | `/reportar` | `POST /api/reports` | Reporte con datos válidos y campos vacíos |
| HU-04 | `/reportar` | `GET /api/reports/mis-reportes` | Estado vacío y reporte recién creado |
| HU-05 | `/reportar` / Swagger | `GET/POST/PUT/DELETE /api/categories`, `/api/locations` | Permisos por rol |
| HU-06–HU-12 | Diseño siguiente | Adjuntos, comentarios, SLA, dashboard | Casos de uso y prototipo de la siguiente iteración |

## Orden recomendado para una entrevista

Presentaría HU-01 a HU-05 como alcance demostrado. Luego enseñaría HU-06, HU-09 y HU-11 como evolución priorizada: resuelven evidencia, responsabilidad y seguimiento, que son los tres puntos que más valor agregan al problema original.
