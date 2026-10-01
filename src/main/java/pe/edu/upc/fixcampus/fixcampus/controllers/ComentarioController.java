package pe.edu.upc.fixcampus.fixcampus.controllers;

import java.util.ArrayList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentariosPorReporteDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;

import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IComentarioService;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@PreAuthorize("hasRole('ADMIN')")
public class ComentarioController {
    private final IComentarioService service;

    public ComentarioController(IComentarioService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Listar comentarios", description = "Si se indica reporteId, muestra solo los comentarios de ese reporte. Solo para administradores.")
    public List<ComentarioDTO> listar(@Parameter(description = "ID del reporte del que se quieren ver comentarios") @RequestParam(required = false) Long reporteId) {
        List<Comentario> lista = reporteId == null ? service.listar()
                : service.buscarPorReporte(reporteId);
        return convertirLista(lista);
    }

    @GetMapping("/estadisticas/por-reporte")
    @Operation(summary = "Contar comentarios de un usuario por reporte", description = "Une comentarios con reportes y usuarios. Para el correo indicado, cuenta cuántos comentarios escribió en cada reporte. Solo para administradores.")
    public List<ComentariosPorReporteDTO> comentariosPorReporte(
            @Parameter(description = "Correo del usuario que escribió los comentarios") @RequestParam String correo) {
        return service.contarPorReporteYCorreo(correo);
    }

    @GetMapping("/{id}")
    public ComentarioDTO buscar(@PathVariable Long id) { return convertir(service.buscarPorId(id)); }

    @PostMapping
    public ResponseEntity<ComentarioDTO> crear(@Valid @RequestBody ComentarioDTO datos) {
        return ResponseEntity.status(201).body(convertir(service.registrar(datos)));
    }

    @PutMapping("/{id}")
    public ComentarioDTO actualizar(@PathVariable Long id, @Valid @RequestBody ComentarioDTO datos) {
        return convertir(service.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ComentarioDTO convertir(Comentario comentario) {
        ComentarioDTO dto = new ComentarioDTO();
        dto.setIdComentario(comentario.getIdComentario());
        dto.setReporteId(comentario.getReporte().getIdReporte());
        dto.setUsuarioId(comentario.getUsuario().getIdUsuario());
        dto.setTextoComentario(comentario.getTextoComentario());
        dto.setFechaComentario(comentario.getFechaComentario());
        return dto;
    }

    private List<ComentarioDTO> convertirLista(List<Comentario> comentarios) {
        List<ComentarioDTO> lista = new ArrayList<>();
        for (Comentario comentario : comentarios) {
            lista.add(convertir(comentario));
        }
        return lista;
    }
}
