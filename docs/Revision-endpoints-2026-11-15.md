# Revisión de endpoints del 15/11/2026

En Swagger, un JSON con un carácter extra después de la llave final en POST /registro devolvía 401. Se reprodujo también con JSON incompleto. El problema estaba en el manejo del error: el formato incorrecto no tenía un manejador propio y el despacho interno de errores volvía a exigir autenticación.

GlobalExceptionHandler ahora responde 400 con un mensaje para revisar comillas, comas y caracteres sobrantes. SecurityConfig permite el despacho interno de tipo ERROR para conservar el código original de otros errores HTTP. Las peticiones normales a rutas protegidas siguen exigiendo JWT; una petición directa a /error sin token sigue devolviendo 401.

Se comprobó el backend local iniciado desde IntelliJ en el puerto 8080. La revisión cubrió las 59 operaciones publicadas en /v3/api-docs, con 121 peticiones HTTP. Incluyó creación, lectura, edición y eliminación en las ocho entidades; registro, login, consultas, estadísticas y sugerencia de categoría de Gemini. Se verificaron también JSON incompleto en cada POST y PUT, parámetros numéricos incorrectos, parámetros obligatorios ausentes, métodos incorrectos, permisos y conflictos al eliminar registros relacionados. Los registros temporales se eliminaron al terminar.

Las 12 pruebas de Maven pasaron y se generó el JAR. RegistroHttpTest prueba el servidor HTTP con una base H2 de prueba: registro sin token, correo duplicado, JSON incompleto, caracteres sobrantes, login incorrecto, acceso protegido y método incorrecto.

Para pegar JSON en Swagger, copiar únicamente desde la llave inicial hasta la final, sin las marcas del bloque de código. Si el correo ya fue registrado, usar otro o iniciar sesión con la cuenta existente: cambiar solo el nombre no cambia el correo.

Estos resultados corresponden al entorno local. No verifican Render ni todas las combinaciones posibles de datos.
