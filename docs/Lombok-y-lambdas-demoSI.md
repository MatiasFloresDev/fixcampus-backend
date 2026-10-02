# Lombok y lambdas de los ejemplos de clase

## Lambdas utilizadas

Se revisaron los proyectos locales demoSI y demoSI_seguridad. FixCampus utiliza únicamente las cinco lambdas de configuración que aparecen en `demoSI_seguridad/demoSI/src/main/java/pe/edu/upc/demosi/securities/SecurityConfig.java`, líneas 58, 60, 66, 86 y 87.

| Patrón del ejemplo | Uso en FixCampus |
|---|---|
| `csrf -> csrf.disable()` | Desactiva CSRF para la API autenticada con Bearer token |
| `session -> session.sessionCreationPolicy(...)` | Configura STATELESS: el cliente envía el token en cada petición |
| `auth -> auth.requestMatchers(...)` | Mantiene login, registro y Swagger públicos; las demás rutas requieren autenticación |
| `oauth2 -> oauth2.jwt(...)` | Configura la validación de tokens JWT |
| `jwt -> jwt.jwtAuthenticationConverter(...)` | Convierte los roles del JWT en permisos de Spring Security |

Las cinco están exclusivamente en `securities/SecurityConfig.java`. Se adaptaron las rutas al proyecto: `/login`, `/registro` y Swagger. Se mantienen HS512, BCrypt y permisos por rol. Los demás controladores, servicios y conversiones conservan `for` e `if`; no se añadieron otras lambdas, streams, referencias a métodos ni ternarios.

Los demos también contienen otros usos de lambdas. La autorización del usuario permite los patrones de esos ejemplos, pero no obliga a convertir todo el proyecto a lambdas. Se eligió esta configuración porque elimina las implementaciones anónimas de Customizer y coincide con la forma del ejemplo de seguridad.

## Lombok en este proyecto

Lombok no aparece en los archivos de los dos demos revisados: se incorpora por pedido expreso del usuario. La dependencia `org.projectlombok:lombok` está en `pom.xml` con scope `provided`; su versión 1.18.46 la administra Spring Boot. Maven también configura el procesador de anotaciones para generar los métodos durante la compilación.

Se aplicó en las ocho entidades y los 26 DTO:

- `@Getter` genera los métodos que leen cada campo, como `getNombre()`.
- `@Setter` genera los métodos que modifican cada campo, como `setNombre(String nombre)`, en las clases que ya tenían setters.
- Los DTO que solo tenían getters siguen únicamente con `@Getter`. Los constructores existentes permanecen escritos.

Se sustituyeron 334 métodos repetitivos por estas anotaciones. Los nombres y campos de entrada/salida se conservan. Lombok no cambia las consultas, tablas, validaciones ni permisos; tampoco cifra contraseñas. BCrypt sigue realizando el hash.

Por ejemplo, en `entities/Usuario.java`, `@Getter` y `@Setter` generan `getNombre()` y `setNombre(...)`, usados por servicios y controladores. En `dtos/UsuarioDTOList.java` generan los accesores para la respuesta de Swagger. Ese DTO sigue sin campos de contraseña, por lo que Lombok no añade una contraseña a la respuesta.

```java
@Getter
@Setter
public class CategoriaDTOInsert {
    @NotBlank
    private String nombre;
    private String descripcion;
}
```

El fragmento ilustra las anotaciones; la clase real conserva sus límites `@Size` y sus constructores. Aunque no se vean los métodos escritos, el resto del código puede seguir llamando `dto.getNombre()` y `dto.setNombre("Electricidad")`.

## Cómo explicarlo

“Lombok me permite evitar escribir getters y setters repetidos. Maven genera esos métodos al compilar; los DTO mantienen los datos y las validaciones definidos. En seguridad uso las mismas lambdas de configuración que aparecen en demoSI_seguridad, adaptando las rutas a FixCampus.”

En IntelliJ, recargar el proyecto Maven después de cambiar el pom. Si el editor no reconoce los métodos generados, comprobar el soporte de Lombok y el procesamiento de anotaciones en la configuración del IDE. La compilación Maven es la comprobación de que los métodos fueron generados.

Referencias oficiales: [Maven](https://projectlombok.org/setup/maven), [Getter y Setter](https://projectlombok.org/features/GetterSetter).

## Comprobación de esta actualización

La verificación inicial de Lombok aprobó cinco pruebas. Posteriormente, para ajustar la estructura a la solicitada por el usuario, se conservó únicamente FixcampusApplicationTests.contextLoads y se retiraron las clases adicionales. Maven clean verify comprueba ahora esa única prueba de arranque. La inspección de las clases compiladas confirmó los getters y setters generados. Las pruebas HTTP sobre PostgreSQL local verificaron 54 operaciones de Swagger con 121 peticiones: ocho CRUD, diez consultas, registro, login, HS512 y permisos. Los datos temporales fueron eliminados y los conteos iniciales restaurados.
