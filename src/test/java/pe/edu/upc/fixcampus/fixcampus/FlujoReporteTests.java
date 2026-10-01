package pe.edu.upc.fixcampus.fixcampus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pe.edu.upc.fixcampus.fixcampus.repositories.IRolRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import pe.edu.upc.fixcampus.fixcampus.repositories.IReporteRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUsuarioRepository;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.repositories.ICategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUbicacionRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlujoReporteTests {

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

    private final ObjectMapper json = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();
    private Long reporteCreado;
    private String correoCreado;
    private Long categoriaCreada;
    private Long ubicacionCreada;

    @Autowired private ICategoriaRepository categoriaRepository;
    @Autowired private IUbicacionRepository ubicacionRepository;

    @Autowired
    private IReporteRepository reporteRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @AfterEach
    void limpiarDatosDePrueba() {
        if (reporteCreado != null) reporteRepository.deleteById(reporteCreado);
        if (correoCreado != null) {
            Optional<Usuario> usuario = usuarioRepository.findByCorreo(correoCreado);
            if (usuario.isPresent()) {
                usuarioRepository.delete(usuario.get());
            }
        }
        if (categoriaCreada != null) categoriaRepository.deleteById(categoriaCreada);
        if (ubicacionCreada != null) ubicacionRepository.deleteById(ubicacionCreada);
    }

    @Test
    void usuarioSeRegistraYVeSuReporteGuardado() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNombre("Equipamiento de prueba");
        categoria.setDescripcion("Proyectores");
        categoriaCreada = categoriaRepository.save(categoria).getIdCategoria();
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setCampus("Campus de prueba");
        ubicacion.setEdificio("A");
        ubicacion.setPiso(3);
        ubicacion.setZona("Aula 301");
        ubicacion.setTipo("AULA");
        ubicacionCreada = ubicacionRepository.save(ubicacion).getIdUbicacion();

        String base = "http://localhost:" + port;
        String correo = "prueba" + System.nanoTime() + "@example.com";
        correoCreado = correo;
        String registro = "{\"nombre\":\"Ana\",\"apellido\":\"Torres\",\"correo\":\""
                + correo + "\",\"password\":\"clave123\"}";
        assertThat(enviar(base + "/registro", registro, null).statusCode()).isEqualTo(201);

        String login = "{\"correo\":\"" + correo + "\",\"password\":\"clave123\"}";
        HttpResponse<String> respuestaLogin = enviar(base + "/login", login, null);
        assertThat(respuestaLogin.statusCode()).isEqualTo(200);
        JsonNode sesion = json.readTree(respuestaLogin.body());
        String token = sesion.get("token").asText();
        assertThat(sesion.get("idUsuario").asLong()).isPositive();

        JsonNode categorias = json.readTree(consultar(base + "/api/categories", token).body());
        JsonNode ubicaciones = json.readTree(consultar(base + "/api/locations", token).body());
        assertThat(categorias.size()).isPositive();
        assertThat(ubicaciones.size()).isPositive();

        String reporte = "{\"categoriaId\":" + categoriaCreada
                + ",\"ubicacionId\":" + ubicacionCreada
                + ",\"titulo\":\"Proyector averiado\",\"descripcion\":\"No enciende\""
                + ",\"detalleUbicacion\":\"Aula 301\",\"prioridad\":\"MEDIA\",\"estado\":\"ABIERTO\"}";
        HttpResponse<String> respuestaReporte = enviar(base + "/api/reports", reporte, token);
        assertThat(respuestaReporte.statusCode()).isEqualTo(201);
        reporteCreado = json.readTree(respuestaReporte.body()).get("idReporte").asLong();

        HttpResponse<String> misReportes = consultar(base + "/api/reports/mis-reportes", token);
        assertThat(misReportes.statusCode()).isEqualTo(200);
        assertThat(misReportes.body()).contains("Proyector averiado", "Aula 301");
    }

    private HttpResponse<String> enviar(String url, String body, String token) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body));
        if (token != null) request.header("Authorization", "Bearer " + token);
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> consultar(String url, String token) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create(url))
                .header("Authorization", "Bearer " + token).GET().build(),
                HttpResponse.BodyHandlers.ofString());
    }
}
