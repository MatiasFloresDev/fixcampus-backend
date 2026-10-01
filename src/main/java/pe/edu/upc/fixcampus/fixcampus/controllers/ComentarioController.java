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
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentariosPorReporteDTO;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'TECNICO')")
    public ResponseEntity<List<ComentarioDTOList>> listar(
            @RequestParam(required = false) Long reporteId,
            Authentication authentication) {

        List<Comentario> comentarios;

        if (reporteId != null) {

            Reporte reporte =
                    reporteService.buscarPorId(reporteId);

            verificarAccesoReporte(
                    reporte,
                    authentication
            );

            comentarios =
                    comentarioService.buscarPorReporte(
                            reporteId
                    );

        } else {

            if (!esAdministradorOTecnico(authentication)) {
                throw new AccessDeniedException(
                        "No tienes permiso para listar todos los comentarios"
                );
            }

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

    @GetMapping("/estadisticas/por-reporte")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ComentariosPorReporteDTO>> comentariosPorReporte(
            @RequestParam String correo) {

        List<ComentariosPorReporteDTO> lista =
                comentarioService
                        .contarPorReporteYCorreo(
                                correo
                        );

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'TECNICO')")
    public ResponseEntity<ComentarioDTOList> buscarPorId(
            @PathVariable Long id,
            Authentication authentication) {

        Comentario comentario =
                comentarioService.buscarPorId(id);

        verificarAccesoReporte(
                comentario.getReporte(),
                authentication
        );

        ComentarioDTOList dto =
                modelMapper.map(
                        comentario,
                        ComentarioDTOList.class
                );

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'TECNICO')")
    public ResponseEntity<ComentarioDTOList> registrar(
            @Valid @RequestBody ComentarioDTOInsert dto,
            Authentication authentication) {

        Reporte reporte =
                reporteService.buscarPorId(
                        dto.getReporteId()
                );

        verificarAccesoReporte(
                reporte,
                authentication
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'TECNICO')")
    public ResponseEntity<ComentarioDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ComentarioDTOInsert dto,
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

    private void verificarAccesoReporte(
            Reporte reporte,
            Authentication authentication) {

        if (esAdministradorOTecnico(authentication)) {
            return;
        }

        boolean propietario =
                reporte.getUsuarioReportante()
                        .getCorreo()
                        .equalsIgnoreCase(
                                authentication.getName()
                        );

        if (!propietario) {
            throw new AccessDeniedException(
                    "No tienes permiso para acceder a los comentarios de este reporte"
            );
        }
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

    private boolean esAdministradorOTecnico(
            Authentication authentication) {

        for (GrantedAuthority autoridad :
                authentication.getAuthorities()) {

            if ("ROLE_ADMIN".equals(
                    autoridad.getAuthority())
                    || "ROLE_TECNICO".equals(
                    autoridad.getAuthority())) {

                return true;
            }
        }

        return false;
    }
}