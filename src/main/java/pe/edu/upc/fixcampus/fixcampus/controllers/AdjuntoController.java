package pe.edu.upc.fixcampus.fixcampus.controllers;

import org.springframework.security.core.GrantedAuthority;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTOList;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;

import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IAdjuntoService;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
public class AdjuntoController {
    private final ModelMapper modelMapper;
    private final IAdjuntoService service;
    public AdjuntoController(IAdjuntoService service, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.service = service;
    }

    @GetMapping("/reporte/{reporteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @Operation(summary = "Listar evidencias de un reporte", description = "Solo permite consultar evidencias del propio reporte o de un administrador.")
    public ResponseEntity<List<AdjuntoDTOList>> listarPorReporte(@PathVariable Long reporteId, Authentication authentication) {
        Reporte reporte = service.buscarReporte(reporteId);
        verificarPermiso(reporte, authentication);
        return ResponseEntity.ok(convertirLista(service.buscarPorReporte(reporteId)));
    }

    private void verificarPermiso(Reporte reporte, Authentication authentication) {
        boolean admin = false;
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                admin = true;
                break;
            }
        }
        if (!admin && !reporte.getUsuarioReportante().getIdUsuario().equals(Long.valueOf(authentication.getName()))) {
            throw new org.springframework.security.access.AccessDeniedException("No tienes permiso para ver esta evidencia");
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar adjuntos", description = "Muestra todos los adjuntos sin parámetros. Solo administradores.")
    public ResponseEntity<List<AdjuntoDTOList>> listar() {
        return ResponseEntity.ok(convertirLista(service.listar()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdjuntoDTOList> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(convertir(service.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdjuntoDTOList> crear(@Valid @RequestBody AdjuntoDTOInsert datos) {
        Adjunto guardado = service.registrar(datos);
        AdjuntoDTOList respuesta = convertir(guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdjuntoDTOList> actualizar(@PathVariable Long id, @Valid @RequestBody AdjuntoDTOInsert datos) {
        return ResponseEntity.ok(convertir(service.actualizar(id, datos)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private AdjuntoDTOList convertir(Adjunto adjunto) {
        AdjuntoDTOList dto = modelMapper.map(adjunto, AdjuntoDTOList.class);
        dto.setReporteId(adjunto.getReporte().getIdReporte());
        return dto;
    }

    private List<AdjuntoDTOList> convertirLista(List<Adjunto> adjuntos) {
        return adjuntos.stream()
                .map(item -> convertir(item))
                .toList();
    }
}
