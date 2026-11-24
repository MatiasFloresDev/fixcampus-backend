package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pe.edu.upc.fixcampus.fixcampus.entities.Notificacion;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.INotificacionRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.INotificacionService;

@Service
public class NotificacionServiceImplement implements INotificacionService {
    private final INotificacionRepository repositorio;
    private final RestClient cliente;
    private final String servicio;
    private final String plantilla;
    private final String clavePublica;
    private final String clavePrivada;
    private final String correoAdmin;
    private final String url;

    public NotificacionServiceImplement(INotificacionRepository repositorio,
            @Value("${EMAILJS_SERVICE_ID:}") String servicio,
            @Value("${EMAILJS_TEMPLATE_ID:}") String plantilla,
            @Value("${EMAILJS_PUBLIC_KEY:}") String clavePublica,
            @Value("${EMAILJS_PRIVATE_KEY:}") String clavePrivada,
            @Value("${CORREO_ADMIN_NOTIFICACIONES:}") String correoAdmin,
            @Value("${EMAILJS_URL:https://api.emailjs.com/api/v1.0/email/send}") String url) {
        this.repositorio = repositorio;
        this.servicio = servicio;
        this.plantilla = plantilla;
        this.clavePublica = clavePublica;
        this.clavePrivada = clavePrivada;
        this.correoAdmin = correoAdmin;
        this.url = url;
        JdkClientHttpRequestFactory fabrica = new JdkClientHttpRequestFactory(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build());
        fabrica.setReadTimeout(Duration.ofSeconds(10));
        this.cliente = RestClient.builder().requestFactory(fabrica).build();
    }

    @Override
    public List<Notificacion> listar() { return repositorio.findAllByOrderByFechaDesc(); }

    @Override
    public Notificacion registrar(Reporte reporte, String tipo) {
        Notificacion notificacion = new Notificacion();
        notificacion.setReporte(reporte);
        notificacion.setTipo(tipo);
        notificacion.setDestinatario("NUEVO_REPORTE".equals(tipo) ? correoAdmin : reporte.getUsuarioReportante().getCorreo());
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setEstado("PENDIENTE");
        notificacion = repositorio.save(notificacion);
        return enviar(notificacion);
    }

    private Notificacion enviar(Notificacion notificacion) {
        if (servicio.isBlank() || plantilla.isBlank() || clavePublica.isBlank() || notificacion.getDestinatario().isBlank()) {
            notificacion.setEstado("ERROR");
            notificacion.setMensaje("Falta configurar el servicio de correo o el destinatario.");
            return repositorio.save(notificacion);
        }
        Reporte reporte = notificacion.getReporte();
        String asunto = "NUEVO_REPORTE".equals(notificacion.getTipo()) ? "Nuevo reporte #" : "Reporte resuelto #";
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("service_id", servicio);
        cuerpo.put("template_id", plantilla);
        cuerpo.put("user_id", clavePublica);
        if (!clavePrivada.isBlank()) cuerpo.put("accessToken", clavePrivada);
        cuerpo.put("template_params", Map.of("to_email", notificacion.getDestinatario(), "subject", asunto + reporte.getIdReporte(),
                "reporte_titulo", reporte.getTitulo(), "reporte_estado", reporte.getEstado(), "reporte_detalle", reporte.getDescripcion()));
        try {
            cliente.post().uri(url).contentType(MediaType.APPLICATION_JSON).body(cuerpo).retrieve().toBodilessEntity();
            notificacion.setEstado("ENVIADA");
            notificacion.setMensaje("El proveedor aceptó el envío del correo.");
        } catch (RestClientException error) {
            notificacion.setEstado("ERROR");
            notificacion.setMensaje("No se pudo enviar el correo. El reporte permanece guardado; puedes reintentar desde Notificaciones.");
        }
        return repositorio.save(notificacion);
    }

    @Override
    public Notificacion reintentar(Long id) {
        Notificacion notificacion = repositorio.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada"));
        if ("ENVIADA".equals(notificacion.getEstado())) return notificacion;
        if ("NUEVO_REPORTE".equals(notificacion.getTipo())) notificacion.setDestinatario(correoAdmin);
        return enviar(notificacion);
    }

    @Override
    public void eliminarPorReporte(Long id) { repositorio.deleteAll(repositorio.findByReporte_IdReporte(id)); }
}
