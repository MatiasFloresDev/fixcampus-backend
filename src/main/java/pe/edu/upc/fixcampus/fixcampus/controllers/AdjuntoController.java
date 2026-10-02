package pe.edu.upc.fixcampus.fixcampus.controllers;

import java.util.ArrayList;
import org.springframework.security.core.GrantedAuthority;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;

import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IAdjuntoService;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
public class AdjuntoController {
    private final IAdjuntoService service;
    public AdjuntoController(IAdjuntoService service) { this.service = service; }

    @GetMapping("/reporte/{reporteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @Operation(summary = "Listar evidencias de un reporte", description = "Solo permite consultar evidencias del propio reporte o de un administrador.")
    public List<AdjuntoDTO> listarPorReporte(@PathVariable Long reporteId, Authentication authentication) {
        Reporte reporte = service.buscarReporte(reporteId);
        verificarPermiso(reporte, authentication);
        return convertirLista(service.buscarPorReporte(reporteId));
    }

    private void verificarPermiso(Reporte reporte, Authentication authentication) {
        boolean admin = false;
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                admin = true;
                break;
            }
        }
        if (!admin && !reporte.getUsuarioReportante().getCorreo().equalsIgnoreCase(authentication.getName())) {
            throw new org.springframework.security.access.AccessDeniedException("No tienes permiso para ver esta evidencia");
        }
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar adjuntos", description = "Muestra todos los adjuntos sin parámetros. Solo administradores.")
    public List<AdjuntoDTO> listar() {
        return convertirLista(service.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdjuntoDTO buscar(@PathVariable Long id) { return convertir(service.buscarPorId(id)); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdjuntoDTO> crear(@Valid @RequestBody AdjuntoDTO datos) {
        Adjunto guardado = service.registrar(datos);
        AdjuntoDTO respuesta = convertir(guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdjuntoDTO actualizar(@PathVariable Long id, @Valid @RequestBody AdjuntoDTO datos) {
        return convertir(service.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private AdjuntoDTO convertir(Adjunto adjunto) {
        AdjuntoDTO dto = new AdjuntoDTO();
        dto.setIdAdjunto(adjunto.getIdAdjunto());
        dto.setReporteId(adjunto.getReporte().getIdReporte());
        dto.setNombreArchivo(adjunto.getNombreArchivo());
        dto.setUrlArchivo(adjunto.getUrlArchivo());
        dto.setTipoArchivo(adjunto.getTipoArchivo());
        dto.setFechaSubida(adjunto.getFechaSubida());
        return dto;
    }

    private List<AdjuntoDTO> convertirLista(List<Adjunto> adjuntos) {
        List<AdjuntoDTO> lista = new ArrayList<>();
        for (Adjunto adjunto : adjuntos) {
            lista.add(convertir(adjunto));
        }
        return lista;
    }
}
