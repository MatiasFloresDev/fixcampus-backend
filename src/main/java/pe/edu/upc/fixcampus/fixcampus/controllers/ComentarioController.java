package pe.edu.upc.fixcampus.fixcampus.controllers;

import pe.edu.upc.fixcampus.fixcampus.dtos.ComentariosPorReporteDTO;
import java.util.ArrayList;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTOList;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTOInsert;
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
    @Operation(summary = "Listar comentarios", description = "Muestra todos los comentarios sin parámetros. Solo administradores.")
    public List<ComentarioDTOList> listar() {
        return convertirLista(service.listar());
    }


    @GetMapping("/{id}")
    public ComentarioDTOList buscar(@PathVariable Long id) { return convertir(service.buscarPorId(id)); }

    @PostMapping
    public ResponseEntity<ComentarioDTOList> crear(@Valid @RequestBody ComentarioDTOInsert datos) {
        Comentario guardado = service.registrar(datos);
        ComentarioDTOList respuesta = convertir(guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    public ComentarioDTOList actualizar(@PathVariable Long id, @Valid @RequestBody ComentarioDTOInsert datos) {
        return convertir(service.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ComentarioDTOList convertir(Comentario comentario) {
        ComentarioDTOList dto = new ComentarioDTOList();
        dto.setIdComentario(comentario.getIdComentario());
        dto.setReporteId(comentario.getReporte().getIdReporte());
        dto.setUsuarioId(comentario.getUsuario().getIdUsuario());
        dto.setTextoComentario(comentario.getTextoComentario());
        dto.setFechaComentario(comentario.getFechaComentario());
        return dto;
    }

    private List<ComentarioDTOList> convertirLista(List<Comentario> comentarios) {
        List<ComentarioDTOList> lista = new ArrayList<>();
        for (Comentario comentario : comentarios) {
            lista.add(convertir(comentario));
        }
        return lista;
    }
    @GetMapping("/estadisticas/por-reporte")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Contar comentarios por incidencia", description = "Consulta 10: un LEFT JOIN y COUNT. Cuenta comentarios por incidencia e incluye las incidencias sin comentarios. No pide parámetros. Solo administradores.")
    public List<ComentariosPorReporteDTO> contarPorReporte() {
        return service.contarPorReporte();
    }
    @GetMapping("/buscar-texto")
    @Operation(summary = "Buscar comentarios por texto", description = "Consulta 9, simple: busca comentarios que contengan la palabra indicada. Ejemplo: texto=lámpara. Solo administradores.")
    public List<ComentarioDTOList> buscarPorTexto(@RequestParam String texto) {
        return convertirLista(service.buscarPorTexto(texto));
    }
}
