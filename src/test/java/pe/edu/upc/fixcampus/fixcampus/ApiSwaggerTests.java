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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiSwaggerTests {

    @Autowired
    private IRolRepository rolesDePrueba;

    @Autowired
    private IUsuarioService usuariosDePrueba;

    @BeforeEach
    void prepararCuentas() {
        PreparacionPruebas.crearCuentas(rolesDePrueba, usuariosDePrueba);
    }

    @Value("${local.server.port}")
    private int port;

    @Test
    void swaggerMuestraLasOchoTablasYUsuariosSinContrasena() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String base = "http://localhost:" + port;

        HttpResponse<String> swagger = client.send(
                HttpRequest.newBuilder(URI.create(base + "/swagger-ui/index.html")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertThat(swagger.statusCode()).isEqualTo(200);

        HttpResponse<String> docs = client.send(
                HttpRequest.newBuilder(URI.create(base + "/v3/api-docs")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertThat(docs.statusCode()).isEqualTo(200);
        JsonNode paths = new ObjectMapper().readTree(docs.body()).get("paths");
        assertThat(paths.get("/api/users").get("get").has("parameters")).isFalse();
        assertThat(paths.get("/api/recomendaciones").get("get").has("parameters")).isFalse();
        assertThat(paths.get("/registro").get("post").get("security").size()).isZero();
        assertThat(paths.get("/login").get("post").get("security").size()).isZero();
        assertThat(paths.get("/api/reports/estadisticas/por-categoria")).isNotNull();
        assertThat(paths.get("/api/attachments/estadisticas/por-usuario")).isNotNull();
        for (String ruta : new String[] {"/api/categories", "/api/reports", "/api/users",
                "/api/roles", "/api/locations", "/api/comments",
                "/api/attachments", "/api/recomendaciones"}) {
            assertThat(docs.body()).contains(ruta);
        }
        assertThat(docs.body()).contains("Contar incidencias por usuario y mes");
        assertThat(docs.body()).contains("Nombre exacto de la categoría; consulta con JOIN");
        assertThat(docs.body()).contains("/api/reports/estadisticas/por-campus");
        assertThat(docs.body()).contains("/api/comments/estadisticas/por-reporte");

        HttpResponse<String> login = client.send(
                HttpRequest.newBuilder(URI.create(base + "/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "{\"correo\":\"admin@fixcampus.com\",\"password\":\"admin123\"}"))
                        .build(), HttpResponse.BodyHandlers.ofString());
        assertThat(login.statusCode()).isEqualTo(200);
        Matcher token = Pattern.compile("\"token\":\"([^\"]+)\"").matcher(login.body());
        assertThat(token.find()).isTrue();

        HttpResponse<String> usuarios = client.send(
                HttpRequest.newBuilder(URI.create(base + "/api/users"))
                        .header("Authorization", "Bearer " + token.group(1))
                        .GET().build(), HttpResponse.BodyHandlers.ofString());
        assertThat(usuarios.statusCode()).isEqualTo(200);
        assertThat(usuarios.body()).contains("admin@fixcampus.com");
        assertThat(usuarios.body()).doesNotContain("contrasenaHash", "admin123");

        HttpResponse<String> cantidad = client.send(
                HttpRequest.newBuilder(URI.create(base + "/api/users/count"))
                        .header("Authorization", "Bearer " + token.group(1))
                        .GET().build(), HttpResponse.BodyHandlers.ofString());
        assertThat(cantidad.statusCode()).isEqualTo(200);
        assertThat(Long.parseLong(cantidad.body())).isGreaterThanOrEqualTo(2);
    }
}
