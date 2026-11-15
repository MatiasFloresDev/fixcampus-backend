package pe.edu.upc.fixcampus.fixcampus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUsuarioRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IRolRepository;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegistroHttpTest {

    @LocalServerPort
    private int port;

    @Autowired
    private IUsuarioRepository usuarios;

    @Autowired
    private IRolRepository roles;

    private final HttpClient cliente = HttpClient.newHttpClient();

    private HttpResponse<String> post(String ruta, String cuerpo) throws Exception {
        HttpRequest peticion = HttpRequest.newBuilder(URI.create("http://localhost:" + port + ruta))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(cuerpo))
                .build();
        return cliente.send(peticion, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void jsonIncompletoDevuelve400SinPedirToken() throws Exception {
        HttpResponse<String> respuesta = post("/registro", "{\"nombre\":\"Prueba\"");
        assertEquals(400, respuesta.statusCode());
        assertTrue(respuesta.body().contains("El JSON no es válido"));
    }

    @Test
    void caracterDespuesDelJsonDevuelve400SinCrearCuenta() throws Exception {
        String correo = "json-" + UUID.randomUUID() + "@fixcampus.test";
        HttpResponse<String> respuesta = post("/registro", datos(correo) + "\n`");
        assertEquals(400, respuesta.statusCode());
        assertTrue(usuarios.findByCorreo(correo).isEmpty());
    }

    @Test
    void registroPublicoFuncionaYRechazaCorreoDuplicado() throws Exception {
        if (roles.findByNombre("USUARIO").isEmpty()) {
            roles.save(new Rol(null, "USUARIO", "LIMITADO", "Rol de la base de prueba"));
        }
        String correo = "registro-" + UUID.randomUUID() + "@fixcampus.test";
        try {
            assertEquals(201, post("/registro", datos(correo)).statusCode());
            assertEquals(400, post("/registro", datos(correo)).statusCode());
        } finally {
            usuarios.findByCorreo(correo).ifPresent(usuarios::delete);
        }
    }

    @Test
    void loginIncorrectoSigueDevolviendo401() throws Exception {
        assertEquals(401, post("/login", "{\"correo\":\"noexiste@fixcampus.test\",\"password\":\"incorrecta\"}")
                .statusCode());
    }

    @Test
    void usuariosSigueExigiendoAutenticacion() throws Exception {
        HttpRequest peticion = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/users"))
                .GET().build();
        assertEquals(401, cliente.send(peticion, HttpResponse.BodyHandlers.ofString()).statusCode());
    }

    @Test
    void metodoIncorrectoEnRutaPublicaDevuelve405() throws Exception {
        HttpRequest peticion = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/registro"))
                .GET().build();
        assertEquals(405, cliente.send(peticion, HttpResponse.BodyHandlers.ofString()).statusCode());
    }

    @Test
    void corsPermiteAngular4200ComoDemoSI() throws Exception {
        HttpResponse<String> respuesta = preflight("http://localhost:4200");
        assertEquals(200, respuesta.statusCode());
        assertEquals("http://localhost:4200", respuesta.headers().firstValue("Access-Control-Allow-Origin").orElse(null));
        assertTrue(respuesta.headers().firstValue("Access-Control-Allow-Methods").orElse("").contains("POST"));
    }

    @Test
    void corsRechazaUnOrigenNoAutorizado() throws Exception {
        for (String origen : new String[]{"http://localhost:4201", "http://localhost:4000", "https://frontend-pruebas.example"}) {
            HttpResponse<String> respuesta = preflight(origen);
            assertEquals(403, respuesta.statusCode());
            assertNull(respuesta.headers().firstValue("Access-Control-Allow-Origin").orElse(null));
        }
    }

    private HttpResponse<String> preflight(String origen) throws Exception {
        HttpRequest peticion = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/reports"))
                .header("Origin", origen)
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "authorization,content-type")
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody()).build();
        return cliente.send(peticion, HttpResponse.BodyHandlers.ofString());
    }

    private String datos(String correo) {
        return "{\"nombre\":\"Prueba\",\"apellido\":\"Temporal\",\"correo\":\"" + correo
                + "\",\"password\":\"Prueba2026\"}";
    }
}
