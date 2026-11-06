package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.ICategoriaService;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class SugerenciaCategoriaServiceImplementTest {

    private static final String URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-lite:generateContent";
    private static final String DESCRIPCION = "El foco del pasillo se apagó.";

    private final JsonMapper json = JsonMapper.builder().build();
    private final ICategoriaService categorias = mock(ICategoriaService.class);
    private MockRestServiceServer servidor;
    private RestClient cliente;
    private SugerenciaCategoriaServiceImplement servicio;

    @BeforeEach
    void preparar() {
        RestClient.Builder builder = RestClient.builder();
        servidor = MockRestServiceServer.bindTo(builder).build();
        cliente = builder.build();
        servicio = new SugerenciaCategoriaServiceImplement(categorias, json, "clave-ficticia", cliente);
        when(categorias.listar()).thenReturn(List.of(new Categoria(7L, "Electricidad", "Dato interno")));
    }

    @Test
    void enviaElCatalogoMinimoYDevuelveLaCategoriaExistente() {
        String datos = json.writeValueAsString(Map.of(
                "descripcion", DESCRIPCION,
                "categorias", List.of(Map.of("id", 7, "nombre", "Electricidad"))
        ));
        servidor.expect(requestTo(URL)).andExpect(method(HttpMethod.POST))
                .andExpect(header("x-goog-api-key", "clave-ficticia"))
                .andExpect(jsonPath("$.contents[0].parts[0].text").value(datos))
                .andExpect(jsonPath("$.generationConfig.responseJsonSchema.properties.categoriaId.type").value("integer"))
                .andRespond(withSuccess(respuesta("{\"categoriaId\":7}"), MediaType.APPLICATION_JSON));

        assertEquals(7L, servicio.sugerir(DESCRIPCION).getCategoriaId());
        servidor.verify();
    }

    @Test
    void rechazaUnaCategoriaInventada() {
        servidor.expect(requestTo(URL))
                .andRespond(withSuccess(respuesta("{\"categoriaId\":99}"), MediaType.APPLICATION_JSON));

        assertEquals(HttpStatus.BAD_GATEWAY,
                assertThrows(ResponseStatusException.class, () -> servicio.sugerir(DESCRIPCION)).getStatusCode());
        servidor.verify();
    }

    @Test
    void informaQueNoHayCoincidencia() {
        servidor.expect(requestTo(URL))
                .andRespond(withSuccess(respuesta("{\"categoriaId\":0}"), MediaType.APPLICATION_JSON));

        assertNull(servicio.sugerir(DESCRIPCION).getCategoriaId());
        servidor.verify();
    }

    @Test
    void informaCuotaAgotadaSinReintentar() {
        servidor.expect(requestTo(URL)).andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        assertEquals(HttpStatus.TOO_MANY_REQUESTS,
                assertThrows(ResponseStatusException.class, () -> servicio.sugerir(DESCRIPCION)).getStatusCode());
        servidor.verify();
    }

    @Test
    void funcionaSinConfiguracionDeGeminiEnElServidor() {
        var sinClave = new SugerenciaCategoriaServiceImplement(categorias, json, "", cliente);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE,
                assertThrows(ResponseStatusException.class, () -> sinClave.sugerir(DESCRIPCION)).getStatusCode());
        servidor.verify();
    }

    private String respuesta(String texto) {
        return json.writeValueAsString(Map.of("candidates", List.of(
                Map.of("content", Map.of("parts", List.of(Map.of("text", texto))))
        )));
    }
}
