# Java para ejecutar FixCampus

El backend se compila con Java 21, igual que el demoSI de la PC2. El error «configured for JVM target 21 … JDK … 17» significa que IntelliJ está intentando compilarlo con Java 17.

## IntelliJ

Abrir la carpeta que contiene el `pom.xml`: `Trabajo Arqui web/backend/fixcampus`. La carpeta superior `backend` puede tener otra configuración de IntelliJ que no se aplica a este proyecto.

En File → Project Structure:

- Project → SDK: JDK 21. En el equipo de Matías está registrado como `21 Microsoft OpenJDK 21.0.13`.
- Modules → fixcampus → Dependencies → Module SDK: Project SDK 21.
- Language level: SDK default o 21.

En Settings → Build, Execution, Deployment → Build Tools → Maven, usar el SDK del proyecto para importar y ejecutar Maven. La configuración de ejecución de FixcampusApplication también debe heredar el SDK del proyecto, sin una selección anterior de Java 17.

No cambiar la versión del `pom.xml` para ocultar este error. Los archivos `.idea` son locales y no se suben a GitHub: cada integrante debe seleccionar su JDK 21 instalado al abrir el proyecto por primera vez.

## Terminal de Windows

En Command Prompt, para esta terminal:

```bat
set "JAVA_HOME=C:\Users\Matias\.jdks\ms-21.0.13.0.2"
set "PATH=%JAVA_HOME%\bin;%PATH%"
mvnw.cmd -v
mvnw.cmd spring-boot:run
```

`mvnw.cmd -v` debe indicar Java 21. Los compañeros deben usar la ruta de su propio JDK 21. Esto no cambia Java para los demás proyectos.

## Comprobación del 14/11/2026

Se corrigió el SDK del proyecto abierto en IntelliJ: `backend/fixcampus/.idea`, que todavía seleccionaba Java 17. El módulo hereda ahora Java 21 y FixcampusApplication compiló y arrancó desde el IDE.

Se ejecutaron 6 pruebas del backend, 21 de Angular y 71 peticiones HTTP de verificación contra el backend local, con limpieza de los registros temporales. Maven generó el JAR y Angular compiló para producción. Angular mantiene los avisos de tamaño inicial y del módulo CommonJS de Leaflet. Esta comprobación no verifica el despliegue remoto.
