package pe.edu.upc.fixcampus.fixcampus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pe.edu.upc.fixcampus.fixcampus.repositories.IRolRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrudCompletoTests {

    @Autowired
    private IRolRepository rolesDePrueba;

    @Autowired
    private IUsuarioService usuariosDePrueba;

    @BeforeEach
    void prepararCuentas() {
        PreparacionPruebas.crearCuentas(rolesDePrueba, usuariosDePrueba);
    }
    @Value("${local.server.port}") private int port;
    private final ObjectMapper json = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void verificaOchoCrudDiezConsultasYPermisos() throws Exception {
        String token = login("admin@fixcampus.com", "admin123");
        String correo = "crud" + System.nanoTime() + "@example.com";
        long rol = crear("/api/roles", "{\"nombre\":\"PRUEBA\",\"nivelAcceso\":\"BASICO\"}", "idRol", token);
        long categoria = crear("/api/categories", "{\"nombre\":\"Electricidad prueba CRUD\",\"descripcion\":\"Luces\"}", "idCategoria", token);
        JsonNode categoriasSinReportes = leer("/api/categories/reporte-por-categoria", token);
        assertThat(categoriasSinReportes.get(0).get("idCategoria").asLong()).isEqualTo(categoria);
        assertThat(categoriasSinReportes.get(0).get("totalReportes").asLong()).isZero();
        assertThat(leer("/api/categories/buscar-descripcion?palabraClave=Luces", token).get(0).get("idCategoria").asLong()).isEqualTo(categoria);
        assertThat(peticion("POST", "/api/roles", "{\"nombre\":\"\",\"nivelAcceso\":\"BASICO\"}", token).statusCode()).isEqualTo(400);
        long ubicacion = crear("/api/locations", "{\"campus\":\"Campus prueba\",\"edificio\":\"A\",\"piso\":1,\"zona\":\"Pasillo\",\"tipo\":\"AULA\"}", "idUbicacion", token);
        String datosUsuario = "{\"rolId\":" + rol + ",\"nombre\":\"Ana\",\"apellido\":\"Prueba\",\"correo\":\"" + correo + "\",\"password\":\"clave123\",\"estado\":\"ACTIVO\"}";
        long usuario = crear("/api/users", datosUsuario, "idUsuario", token);

        String datosReporte = "{\"usuarioReportanteId\":" + usuario + ",\"tecnicoAsignadoId\":" + usuario
                + ",\"categoriaId\":" + categoria + ",\"ubicacionId\":" + ubicacion
                + ",\"titulo\":\"Luz averiada prueba\",\"descripcion\":\"No enciende\",\"prioridad\":\"MEDIA\",\"estado\":\"RESUELTO\"}";
        long reporte = crear("/api/reports", datosReporte, "idReporte", token);
        assertThat(leer("/api/categories/reporte-por-categoria", token).get(0).get("totalReportes").asLong()).isEqualTo(1);
        assertThat(peticion("GET", "/api/recomendaciones/reporte/" + reporte, null, token).statusCode()).isEqualTo(404);
        JsonNode creado = leer("/api/reports/" + reporte, token);
        assertThat(creado.get("estado").asText()).isEqualTo("ABIERTO");
        assertThat(creado.get("fechaAsignacion").isNull()).isFalse();
        long autor = creado.get("usuarioReportanteId").asLong();
        assertThat(autor).isNotEqualTo(usuario); // El autor se toma de la cuenta autenticada.

        long comentario = crear("/api/comments", "{\"reporteId\":" + reporte + ",\"usuarioId\":" + usuario + ",\"textoComentario\":\"Necesita revisión\"}", "idComentario", token);
        long adjunto = crear("/api/attachments", "{\"reporteId\":" + reporte + ",\"nombreArchivo\":\"luz.jpg\",\"urlArchivo\":\"https://example.com/luz.jpg\",\"tipoArchivo\":\"image/jpeg\"}", "idAdjunto", token);
        long recomendacion = crear("/api/recomendaciones", "{\"reporteId\":" + reporte + ",\"tituloSugerido\":\"Revisar lámpara\",\"resumen\":\"Cambio de lámpara\",\"prioridadSugerida\":\"MEDIA\",\"justificacion\":\"Aula sin luz\"}", "idRecomendacion", token);
        assertThat(leer("/api/recomendaciones/reporte/" + reporte, token).get("idRecomendacion").asLong()).isEqualTo(recomendacion);

        String[] rutas = {"/api/roles", "/api/categories", "/api/locations", "/api/users", "/api/reports", "/api/comments", "/api/attachments", "/api/recomendaciones"};
        long[] ids = {rol, categoria, ubicacion, usuario, reporte, comentario, adjunto, recomendacion};
        for (int i = 0; i < rutas.length; i++) {
            assertThat(peticion("GET", rutas[i], null, token).statusCode()).isEqualTo(200);
            String datos = peticion("GET", rutas[i] + "/" + ids[i], null, token).body();
            if (rutas[i].equals("/api/users")) {
                assertThat(datos).doesNotContain("contrasenaHash", "password", "clave123");
                datos = datosUsuario.replace("Ana", "Ana actualizada");
            } else if (rutas[i].equals("/api/reports")) {
                datos = datosReporte;
            }
            assertThat(peticion("PUT", rutas[i] + "/" + ids[i], datos, token).statusCode()).isEqualTo(200);
        }
        assertThat(leer("/api/users/" + usuario, token).get("nombre").asText()).isEqualTo("Ana actualizada");
        JsonNode actualizado = leer("/api/reports/" + reporte, token);
        assertThat(actualizado.get("usuarioReportanteId").asLong()).isEqualTo(autor);
        assertThat(actualizado.get("estado").asText()).isEqualTo("RESUELTO");
        assertThat(actualizado.get("fechaResolucion").isNull()).isFalse();

        // Las diez consultas conservadas en Swagger.
        assertThat(leer("/api/reports?estado=RESUELTO", token).get(0).get("idReporte").asLong()).isEqualTo(reporte);
        assertThat(leer("/api/reports/estadisticas/por-usuario-mes", token).get(0).get("cantidad").asLong()).isEqualTo(1);
        assertThat(leer("/api/locations?campus=prueba", token).get(0).get("idUbicacion").asLong()).isEqualTo(ubicacion);
        assertThat(leer("/api/reports/prioridad/MEDIA", token).get(0).get("idReporte").asLong()).isEqualTo(reporte);
        assertThat(leer("/api/reports/campus/Campus%20prueba", token).get(0).get("idReporte").asLong()).isEqualTo(reporte);
        assertThat(leer("/api/categories/buscar-descripcion?palabraClave=Luces", token).get(0).get("idCategoria").asLong()).isEqualTo(categoria);
        assertThat(leer("/api/categories/reporte-por-categoria", token).get(0).get("totalReportes").asLong()).isEqualTo(1);
        assertThat(leer("/api/recomendaciones/prioridad?prioridad=MEDIA", token).get(0).get("idRecomendacion").asLong()).isEqualTo(recomendacion);
        assertThat(leer("/api/recomendaciones/por-categoria?nombre=Electricidad%20prueba%20CRUD", token).get(0).get("idRecomendacion").asLong()).isEqualTo(recomendacion);
        assertThat(leer("/api/recomendaciones/reporte/" + reporte, token).get("idRecomendacion").asLong()).isEqualTo(recomendacion);

        String usuarioToken = login("usuario@fixcampus.com", "usuario123");
        assertThat(peticion("GET", "/api/users", null, usuarioToken).statusCode()).isEqualTo(403);
        assertThat(peticion("GET", "/api/recomendaciones", null, usuarioToken).statusCode()).isEqualTo(403);
        assertThat(peticion("GET", "/api/recomendaciones/reporte/" + reporte, null, usuarioToken).statusCode()).isEqualTo(403);
        assertThat(peticion("GET", "/api/categories/reporte-por-categoria", null, usuarioToken).statusCode()).isEqualTo(403);
        assertThat(peticion("PUT", "/api/reports/" + reporte, datosReporte, usuarioToken).statusCode()).isEqualTo(403);
        assertThat(peticion("GET", "/api/users", null, null).statusCode()).isEqualTo(401);
        assertThat(peticion("POST", "/login", "{\"correo\":\"admin@fixcampus.com\",\"password\":\"incorrecta\"}", null).statusCode()).isEqualTo(401);
        assertThat(peticion("POST", "/registro", "{\"nombre\":\"Ana\",\"apellido\":\"Prueba\",\"correo\":\"" + correo + "\",\"password\":\"clave123\"}", null).statusCode()).isEqualTo(400);

        // La FK protege el reporte mientras sus comentarios, adjuntos y recomendación existan.
        assertThat(peticion("DELETE", "/api/reports/" + reporte, null, token).statusCode()).isEqualTo(409);
        assertThat(peticion("GET", "/api/reports/" + reporte, null, token).statusCode()).isEqualTo(200);
        for (int i = 5; i < rutas.length; i++) {
            assertThat(peticion("DELETE", rutas[i] + "/" + ids[i], null, token).statusCode()).isEqualTo(204);
        }
        assertThat(peticion("DELETE", "/api/reports/" + reporte, null, token).statusCode()).isEqualTo(204);
        for (int i = 4; i < rutas.length; i++) {
            assertThat(peticion("GET", rutas[i] + "/" + ids[i], null, token).statusCode()).isEqualTo(404);
        }
        for (int i : new int[]{3, 0, 1, 2}) {
            assertThat(peticion("DELETE", rutas[i] + "/" + ids[i], null, token).statusCode()).isEqualTo(204);
            assertThat(peticion("GET", rutas[i] + "/" + ids[i], null, token).statusCode()).isEqualTo(404);
        }
    }

    private long crear(String ruta, String body, String campoId, String token) throws Exception {
        HttpResponse<String> respuesta = peticion("POST", ruta, body, token);
        assertThat(respuesta.statusCode()).withFailMessage(respuesta.body()).isEqualTo(201);
        return json.readTree(respuesta.body()).get(campoId).asLong();
    }

    private String login(String correo, String password) throws Exception {
        HttpResponse<String> respuesta = peticion("POST", "/login", "{\"correo\":\"" + correo + "\",\"password\":\"" + password + "\"}", null);
        assertThat(respuesta.statusCode()).isEqualTo(200);
        return json.readTree(respuesta.body()).get("token").asText();
    }

    private JsonNode leer(String ruta, String token) throws Exception {
        HttpResponse<String> respuesta = peticion("GET", ruta, null, token);
        assertThat(respuesta.statusCode()).withFailMessage(respuesta.body()).isEqualTo(200);
        return json.readTree(respuesta.body());
    }

    private HttpResponse<String> peticion(String verbo, String ruta, String body, String token) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + ruta));
        if (token != null) request.header("Authorization", "Bearer " + token);
        if (body != null) {
            request.header("Content-Type", "application/json");
            request.method(verbo, HttpRequest.BodyPublishers.ofString(body));
        } else {
            request.method(verbo, HttpRequest.BodyPublishers.noBody());
        }
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }
}
