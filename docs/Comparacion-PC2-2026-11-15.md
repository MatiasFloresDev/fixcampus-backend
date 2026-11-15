# Comparación con el demoSI de la PC2

Referencia: `C:/Users/Matias/Documents/FRONTENTD- ARQUI WEB/PC2/demoSI/demoSI`. Se revisaron sus archivos de seguridad, controlador, servicio, repositorio y DTO. Para Angular se contrastaron el servicio, el modelo y los componentes de `PC2/pc2-flores/pc2_flores`, junto con las referencias anteriores de formularios de demoFront.

| Parte | Patrón comprobado y aplicación en FixCampus |
|---|---|
| CORS | El mismo método del demoSI: `CorsConfiguration`, `setAllowedOrigins(List.of("http://localhost:4200"))`, métodos y cabeceras explícitos, `UrlBasedCorsConfigurationSource` y registro en `/**`. |
| Autenticación | Constructor con UserDetailsService, BCryptPasswordEncoder, DaoAuthenticationProvider y AuthenticationManager, como el ejemplo. |
| JWT | JwtEncoder, JwtClaimsSet, duración de cinco horas y HS512. La construcción del texto de roles usa stream, map y Collectors.joining, como JwtTokenService del demoSI. |
| Controladores | Constructor, DTO, ModelMapper y ResponseEntity; listas con stream, map y toList. Se verificaron los once controladores; InicioController solo redirige a Swagger. |
| Servicios | Interfaces separadas de implementaciones. Las operaciones usan los repositorios de cada entidad y los métodos de JpaRepository. Las consultas agrupadas conservan sus DTO. |
| Repositorios | Los ocho repositorios extienden JpaRepository. |
| Entidades y DTO | Clases normales con campos, constructores y getters/setters explícitos. Sin Lombok ni records. Java 21 en Maven y Docker. |
| Angular | Componentes con archivos TS, HTML y CSS; constructor, ngOnInit, propiedades normales, DTO como clases, HttpClient y Observable en servicios, FormBuilder/FormGroup y subscribe en las pantallas. Rutas con imports y component explícitos, como en la PC2; sin loadComponent ni generación automática de rutas de catálogos. Se conserva Angular Material. |

La revisión posterior eliminó el DTO genérico de catálogos de Angular y separó categorías, ubicaciones y roles en sus DTO, servicios y componentes concretos. Los DTO están en archivos individuales; no utilizan Partial, Record, Omit ni uniones de string/number para representar indistintamente todas las columnas.

## Configuración local y publicada

Por indicación expresa de Matías, SecurityConfig utiliza exactamente el bean CORS del demoSI que compartió: únicamente `http://localhost:4200`. No recibe parámetros, no usa variables de entorno ni recorre orígenes adicionales. El puerto 4000 y los dominios publicados no están autorizados por este bean. Para conectar un frontend desplegado en Render será necesario añadir su URL a setAllowedOrigins; FRONTEND_ORIGINS ya no cambia esta lista.

## Diferencias que necesita FixCampus

El CRUD del examen utiliza una sola tabla. FixCampus conserva relaciones entre entidades, comprobación de propietarios, roles y consultas. El subject del token sigue siendo el ID del usuario porque los controladores lo utilizan para identificar al propietario; no se cambia por un correo sin adaptar los contratos.

Se conserva el manejo de JSON inválido y el despacho interno ERROR para responder con el código correcto. Tampoco se trasladan a FixCampus los permisos públicos de GET y DELETE del ejercicio: sus operaciones siguen protegidas según el rol.

Gemini y Leaflet son integraciones del proyecto que no aparecen en ese CRUD del demoSI. Los guards funcionales de Angular mantienen inject en el archivo de rutas; los componentes y servicios usan constructor. Estas partes son adaptaciones, no ejemplos que se deban atribuir a la profesora.

Los nombres de rutas y los JSON de FixCampus se mantienen para que Angular siga consumiendo el mismo backend. La comparación indica qué patrón se usa; no afirma que todas las líneas de dos aplicaciones distintas sean idénticas.

## Comprobación

El método corsConfigurationSource se comparó con el método del demoSI omitiendo únicamente espacios y coincide. Pasaron 14 pruebas de Maven y se generó el JAR con Java 21. Una instancia temporal del JAR en 8083 verificó el CORS exacto: localhost:4200 permitido; 127.0.0.1:4200, localhost:4000 y localhost:4201 rechazados. El barrido cubrió las 59 operaciones con 121 peticiones, incluidos los permisos y los errores HTTP; se eliminaron los registros temporales. En esa instancia Gemini no tenía la clave configurada y respondió 503, por lo que ese ensayo no certifica una respuesta real de Gemini. La instancia temporal se detuvo al terminar.
