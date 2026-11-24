package pe.edu.upc.fixcampus.fixcampus.controllers;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.TextoIncidenciaDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.SugerenciaCategoriaDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.SugerenciaRecomendacionDTO;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IIntegracionIAService;

@RestController
public class IntegracionIAController {
    private final IIntegracionIAService servicio;
    public IntegracionIAController(IIntegracionIAService servicio) { this.servicio = servicio; }

    @PostMapping("/api/reports/sugerir-categoria")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<SugerenciaCategoriaDTO> categoria(@Valid @RequestBody TextoIncidenciaDTO dto) {
        return ResponseEntity.ok(servicio.sugerirCategoria(dto.getDescripcion()));
    }

    @PostMapping("/api/recomendaciones/sugerir/{reporteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SugerenciaRecomendacionDTO> recomendacion(@PathVariable Long reporteId) {
        return ResponseEntity.ok(servicio.sugerirRecomendacion(reporteId));
    }
}
