package pe.edu.upc.fixcampus.fixcampus.controllers;

import java.util.ArrayList;
import org.springframework.security.core.GrantedAuthority;
import pe.edu.upc.fixcampus.fixcampus.dtos.EvidenciasPorUsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;

import java.io.IOException;
import java.nio.file.Path;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IAdjuntoService;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
public class AdjuntoController {
    @GetMapping("/estadisticas/por-usuario")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Contar evidencias de un usuario", description = "Consulta 19: JOIN, LEFT JOIN y COUNT. Cuenta las evidencias de las incidencias del usuario indicado; si tiene incidencias sin evidencias devuelve cero. Ejemplo: correo=usuario@fixcampus.com.")
    public List<EvidenciasPorUsuarioDTO> evidenciasPorUsuario(@RequestParam String correo) {
        return service.contarEvidenciasPorUsuario(correo);
    }
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @Operation(summary = "Subir evidencia", description = "Guarda una imagen JPG/PNG o PDF de hasta 5 MB asociada a un reporte. Máximo 3 evidencias por reporte.")
    public ResponseEntity<AdjuntoDTO> subir(@RequestParam Long reporteId,
                                             @RequestPart MultipartFile file,
                                             Authentication authentication) throws IOException {
        verificarPermiso(service.buscarReporte(reporteId), authentication);
        return ResponseEntity.status(201).body(convertir(service.subir(reporteId, file)));
    }

    @GetMapping("/files/{fileName:.+}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<Resource> descargar(@PathVariable String fileName, Authentication authentication) {
        Adjunto adjunto = service.buscarPorUrl("/api/attachments/files/" + fileName);
        verificarPermiso(adjunto.getReporte(), authentication);
        Path path = service.rutaArchivo(fileName);
        Resource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentType(mediaType(adjunto.getTipoArchivo()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + adjunto.getNombreArchivo() + "\"")
                .body(resource);
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

    private MediaType mediaType(String contentType) {
        try {
            return MediaType.parseMediaType(contentType);
        } catch (Exception ignored) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar adjuntos", description = "Si se indica tipoArchivo, busca adjuntos cuyo tipo contenga ese texto, sin distinguir mayúsculas. Solo para administradores.")
    public List<AdjuntoDTO> listar(@Parameter(description = "Parte del tipo de archivo, por ejemplo pdf") @RequestParam(required = false) String tipoArchivo) {
        List<Adjunto> lista = tipoArchivo == null || tipoArchivo.isBlank() ? service.listar()
                : service.buscarPorTipo(tipoArchivo);
        return convertirLista(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdjuntoDTO buscar(@PathVariable Long id) { return convertir(service.buscarPorId(id)); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdjuntoDTO> crear(@Valid @RequestBody AdjuntoDTO datos) {
        return ResponseEntity.status(201).body(convertir(service.registrar(datos)));
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
