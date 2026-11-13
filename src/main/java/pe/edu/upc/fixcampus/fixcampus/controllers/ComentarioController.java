package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import pe.edu.upc.fixcampus.fixcampus.dtos.TextoComentarioDTO;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final IComentarioService service;

    public ComentarioController(IComentarioService service, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar comentarios", description = "Muestra todos los comentarios sin parámetros. Solo administradores.")
    public ResponseEntity<List<ComentarioDTOList>> listar() {
        return ResponseEntity.ok(convertirLista(service.listar()));
    }

    @GetMapping("/reporte/{reporteId}")
    @Operation(summary = "Listar comentarios del reporte")
    public ResponseEntity<List<ComentarioDTOList>> listarPorReporte(@PathVariable Long reporteId) {
        return ResponseEntity.ok(convertirLista(service.listarPorReporte(reporteId)));
    }

    @PostMapping("/reporte/{reporteId}")
    @Operation(summary = "Comentar un reporte con la cuenta autenticada")
    public ResponseEntity<ComentarioDTOList> comentar(@PathVariable Long reporteId,
            @Valid @RequestBody TextoComentarioDTO texto, Authentication authentication) {
        ComentarioDTOInsert datos = new ComentarioDTOInsert(reporteId, texto.getTextoComentario().trim());
        datos.setUsuarioId(Long.valueOf(authentication.getName()));
        return crear(datos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDTOList> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(convertir(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<ComentarioDTOList> crear(@Valid @RequestBody ComentarioDTOInsert datos) {
        Comentario guardado = service.registrar(datos);
        ComentarioDTOList respuesta = convertir(guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComentarioDTOList> actualizar(@PathVariable Long id, @Valid @RequestBody ComentarioDTOInsert datos) {
        return ResponseEntity.ok(convertir(service.actualizar(id, datos)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ComentarioDTOList convertir(Comentario comentario) {
        ComentarioDTOList dto = modelMapper.map(comentario, ComentarioDTOList.class);
        dto.setReporteId(comentario.getReporte().getIdReporte());
        dto.setUsuarioId(comentario.getUsuario().getIdUsuario());
        return dto;
    }

    private List<ComentarioDTOList> convertirLista(List<Comentario> comentarios) {
        return comentarios.stream()
                .map(item -> convertir(item))
                .toList();
    }
}
