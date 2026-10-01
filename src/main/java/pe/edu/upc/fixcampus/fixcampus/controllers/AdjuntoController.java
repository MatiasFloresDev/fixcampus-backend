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
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTOList;
import pe.edu.upc.fixcampus.fixcampus.dtos.EvidenciasPorUsuarioDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IAdjuntoService;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IReporteService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
public class AdjuntoController {

    private final IAdjuntoService adjuntoService;
    private final IReporteService reporteService;
    private final ModelMapper modelMapper;

    public AdjuntoController(
            IAdjuntoService adjuntoService,
            IReporteService reporteService,
            ModelMapper modelMapper) {

        this.adjuntoService = adjuntoService;
        this.reporteService = reporteService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/estadisticas/por-usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EvidenciasPorUsuarioDTO>> evidenciasPorUsuario(
            @RequestParam String correo) {

        List<EvidenciasPorUsuarioDTO> lista =
                adjuntoService.contarEvidenciasPorUsuario(correo);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/reporte/{reporteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<AdjuntoDTOList>> listarPorReporte(
            @PathVariable Long reporteId,
            Authentication authentication) {

        Reporte reporte =
                reporteService.buscarPorId(reporteId);

        verificarPermiso(reporte, authentication);

        List<AdjuntoDTOList> lista =
                adjuntoService.buscarPorReporte(reporteId)
                        .stream()
                        .map(adjunto ->
                                modelMapper.map(
                                        adjunto,
                                        AdjuntoDTOList.class
                                )
                        )
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdjuntoDTOList>> listar(
            @RequestParam(required = false) String tipoArchivo) {

        List<Adjunto> adjuntos;

        if (tipoArchivo != null && !tipoArchivo.isBlank()) {
            adjuntos =
                    adjuntoService.buscarPorTipo(tipoArchivo);
        } else {
            adjuntos =
                    adjuntoService.listar();
        }

        List<AdjuntoDTOList> lista = adjuntos
                .stream()
                .map(adjunto ->
                        modelMapper.map(
                                adjunto,
                                AdjuntoDTOList.class
                        )
                )
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdjuntoDTOList> buscarPorId(
            @PathVariable Long id) {

        Adjunto adjunto =
                adjuntoService.buscarPorId(id);

        AdjuntoDTOList dto =
                modelMapper.map(
                        adjunto,
                        AdjuntoDTOList.class
                );

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<AdjuntoDTOList> registrar(
            @Valid @RequestBody AdjuntoDTOInsert dto,
            Authentication authentication) {

        Reporte reporte =
                reporteService.buscarPorId(
                        dto.getReporteId()
                );

        verificarPermiso(reporte, authentication);

        Adjunto adjunto =
                modelMapper.map(
                        dto,
                        Adjunto.class
                );

        adjunto.setReporte(reporte);

        Adjunto guardado =
                adjuntoService.registrar(adjunto);

        AdjuntoDTOList responseDTO =
                modelMapper.map(
                        guardado,
                        AdjuntoDTOList.class
                );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(
                        guardado.getIdAdjunto()
                )
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdjuntoDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AdjuntoDTOInsert dto) {

        Adjunto adjunto =
                modelMapper.map(
                        dto,
                        Adjunto.class
                );

        Adjunto actualizado =
                adjuntoService.actualizar(
                        id,
                        adjunto
                );

        AdjuntoDTOList responseDTO =
                modelMapper.map(
                        actualizado,
                        AdjuntoDTOList.class
                );

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        adjuntoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    private void verificarPermiso(
            Reporte reporte,
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

        boolean propietario =
                reporte.getUsuarioReportante()
                        .getCorreo()
                        .equalsIgnoreCase(
                                authentication.getName()
                        );

        if (!administrador && !propietario) {
            throw new AccessDeniedException(
                    "No tienes permiso para acceder a los adjuntos de este reporte"
            );
        }
    }
}