package pe.edu.upc.fixcampus.fixcampus.controllers;

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
}
