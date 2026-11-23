package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTOList;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTOUpdate;
import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IComentarioService;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IReporteService;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class ComentarioController {

    private final IComentarioService comentarioService;
    private final IReporteService reporteService;
    private final IUsuarioService usuarioService;
    private final ModelMapper modelMapper;

    public ComentarioController(
            IComentarioService comentarioService,
            IReporteService reporteService,
            IUsuarioService usuarioService,
            ModelMapper modelMapper) {

        this.comentarioService = comentarioService;
        this.reporteService = reporteService;
        this.usuarioService = usuarioService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ComentarioDTOList>> listar(
            @RequestParam(required = false) Long reporteId) {

        List<Comentario> comentarios;

        if (reporteId != null) {

            reporteService.buscarPorId(reporteId);

            comentarios =
                    comentarioService.buscarPorReporte(
                            reporteId
                    );

        } else {

            comentarios =
                    comentarioService.listar();
        }

        List<ComentarioDTOList> lista =
                comentarios
                        .stream()
                        .map(comentario ->
                                modelMapper.map(
                                        comentario,
                                        ComentarioDTOList.class
                                )
                        )
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComentarioDTOList> buscarPorId(
            @PathVariable Long id) {

        Comentario comentario =
                comentarioService.buscarPorId(id);

        ComentarioDTOList dto =
                modelMapper.map(
                        comentario,
                        ComentarioDTOList.class
                );

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComentarioDTOList> registrar(
            @Valid @RequestBody ComentarioDTOInsert dto,
            Authentication authentication) {

        Reporte reporte =
                reporteService.buscarPorId(
                        dto.getReporteId()
                );

        Usuario usuario =
                usuarioService.buscarPorCorreo(
                        authentication.getName()
                );

        Comentario comentario =
                modelMapper.map(
                        dto,
                        Comentario.class
                );
        comentario.setIdComentario(null);
        comentario.setReporte(reporte);
        comentario.setUsuario(usuario);

        Comentario guardado =
                comentarioService.registrar(
                        comentario
                );

        ComentarioDTOList responseDTO =
                modelMapper.map(
                        guardado,
                        ComentarioDTOList.class
                );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(
                        guardado.getIdComentario()
                )
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComentarioDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ComentarioDTOUpdate dto,
            Authentication authentication) {

        Comentario comentarioActual =
                comentarioService.buscarPorId(id);

        verificarAutorComentario(
                comentarioActual,
                authentication
        );

        Comentario comentario =
                modelMapper.map(
                        dto,
                        Comentario.class
                );

        Comentario actualizado =
                comentarioService.actualizar(
                        id,
                        comentario
                );

        ComentarioDTOList responseDTO =
                modelMapper.map(
                        actualizado,
                        ComentarioDTOList.class
                );

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        comentarioService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    private void verificarAutorComentario(
            Comentario comentario,
            Authentication authentication) {

        boolean administrador = false;

        for (GrantedAuthority autoridad :
                authentication.getAuthorities()) {

            if ("ROLE_ADMIN".equals(
                    autoridad.getAuthority())) {

                administrador = true;
                break;
            }
        }

        boolean autor =
                comentario.getUsuario()
                        .getCorreo()
                        .equalsIgnoreCase(
                                authentication.getName()
                        );

        if (!administrador && !autor) {
            throw new AccessDeniedException(
                    "No tienes permiso para modificar este comentario"
            );
        }
    }
}
