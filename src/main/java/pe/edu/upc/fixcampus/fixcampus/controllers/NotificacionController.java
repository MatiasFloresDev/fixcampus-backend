package pe.edu.upc.fixcampus.fixcampus.controllers;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.NotificacionDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Notificacion;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.INotificacionService;

@RestController
@RequestMapping("/api/notificaciones")
@PreAuthorize("hasRole('ADMIN')")
public class NotificacionController {
    private final INotificacionService servicio;
    private final ModelMapper modelMapper;
    public NotificacionController(INotificacionService servicio, ModelMapper modelMapper) {
        this.servicio = servicio;
        this.modelMapper = modelMapper;
    }
    private NotificacionDTO convertir(Notificacion registro) {
        NotificacionDTO dto = modelMapper.map(registro, NotificacionDTO.class);
        dto.setReporteId(registro.getReporte().getIdReporte());
        return dto;
    }
    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> listar() {
        return ResponseEntity.ok(servicio.listar().stream().map(n -> convertir(n)).toList());
    }
    @PostMapping("/{id}/reintentar")
    public ResponseEntity<NotificacionDTO> reintentar(@PathVariable Long id) {
        return ResponseEntity.ok(convertir(servicio.reintentar(id)));
    }
}
