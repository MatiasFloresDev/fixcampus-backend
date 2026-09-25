package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.AdjuntoRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.ReporteRepository;

import java.time.LocalDateTime;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
public class AdjuntoController {
    private final AdjuntoRepository repository;
    private final ReporteRepository reporteRepository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    public AdjuntoController(AdjuntoRepository repository, ReporteRepository reporteRepository) {
        this.repository = repository;
        this.reporteRepository = reporteRepository;
    }

    @GetMapping("/reporte/{reporteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @Operation(summary = "Listar evidencias de un reporte", description = "Solo permite consultar evidencias del propio reporte o de un administrador.")
    public List<AdjuntoDTO> listarPorReporte(@PathVariable Long reporteId, Authentication authentication) {
        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado"));
        verificarPermiso(reporte, authentication);
        return repository.findByReporte_IdReporte(reporteId).stream().map(this::convertir).toList();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @Operation(summary = "Subir evidencia", description = "Guarda una imagen JPG/PNG o PDF de hasta 5 MB asociada a un reporte. Máximo 3 evidencias por reporte.")
    public ResponseEntity<AdjuntoDTO> subir(
            @RequestParam Long reporteId,
            @RequestPart MultipartFile file,
            Authentication authentication) throws IOException {
        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado"));
        verificarPermiso(reporte, authentication);
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("El archivo no puede superar los 5 MB");
        }
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        Set<String> tiposPermitidos = Set.of("image/jpeg", "image/png", "application/pdf");
        if (!tiposPermitidos.contains(contentType)) {
            throw new IllegalArgumentException("Solo se permiten archivos JPG, PNG o PDF");
        }
        if (repository.countByReporte_IdReporte(reporteId) >= 3) {
            throw new IllegalArgumentException("Un reporte puede tener como máximo 3 evidencias");
        }

        String original = file.getOriginalFilename() == null ? "evidencia" : Paths.get(file.getOriginalFilename()).getFileName().toString();
        String extension = extension(original, contentType);
        String storedName = UUID.randomUUID() + extension;
        Path directory = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(directory);
        Files.copy(file.getInputStream(), directory.resolve(storedName));

        Adjunto adjunto = new Adjunto();
        adjunto.setReporte(reporte);
        adjunto.setNombreArchivo(original);
        adjunto.setUrlArchivo("/api/attachments/files/" + storedName);
        adjunto.setTipoArchivo(contentType);
        adjunto.setFechaSubida(LocalDateTime.now());
        return ResponseEntity.status(201).body(convertir(repository.save(adjunto)));
    }

    @GetMapping("/files/{fileName:.+}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<Resource> descargar(@PathVariable String fileName, Authentication authentication) {
        Adjunto adjunto = repository.findByUrlArchivo("/api/attachments/files/" + fileName)
                .orElseThrow(() -> new ResourceNotFoundException("Archivo no encontrado"));
        verificarPermiso(adjunto.getReporte(), authentication);
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName).normalize();
        if (!path.startsWith(Paths.get(uploadDir).toAbsolutePath().normalize()) || !Files.exists(path)) {
            throw new ResourceNotFoundException("Archivo no encontrado");
        }
        Resource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentType(mediaType(adjunto.getTipoArchivo()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + adjunto.getNombreArchivo() + "\"")
                .body(resource);
    }

    private void verificarPermiso(Reporte reporte, Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!admin && !reporte.getUsuarioReportante().getCorreo().equalsIgnoreCase(authentication.getName())) {
            throw new org.springframework.security.access.AccessDeniedException("No tienes permiso para ver esta evidencia");
        }
    }

    private String extension(String original, String contentType) {
        String lower = original.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".pdf")) {
            return lower.substring(lower.lastIndexOf('.'));
        }
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> ".pdf";
        };
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
        List<Adjunto> lista = tipoArchivo == null || tipoArchivo.isBlank() ? repository.findAll()
                : repository.findByTipoArchivoContainingIgnoreCase(tipoArchivo);
        return lista.stream().map(this::convertir).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdjuntoDTO buscar(@PathVariable Long id) { return convertir(buscarEntidad(id)); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdjuntoDTO> crear(@Valid @RequestBody AdjuntoDTO datos) {
        Adjunto adjunto = new Adjunto();
        copiarDatos(adjunto, datos);
        adjunto.setFechaSubida(LocalDateTime.now());
        return ResponseEntity.status(201).body(convertir(repository.save(adjunto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdjuntoDTO actualizar(@PathVariable Long id, @Valid @RequestBody AdjuntoDTO datos) {
        Adjunto adjunto = buscarEntidad(id);
        copiarDatos(adjunto, datos);
        return convertir(repository.save(adjunto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repository.delete(buscarEntidad(id));
        return ResponseEntity.noContent().build();
    }

    private Adjunto buscarEntidad(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Adjunto no encontrado"));
    }

    private void copiarDatos(Adjunto adjunto, AdjuntoDTO datos) {
        adjunto.setReporte(reporteRepository.findById(datos.getReporteId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado")));
        adjunto.setNombreArchivo(datos.getNombreArchivo());
        adjunto.setUrlArchivo(datos.getUrlArchivo());
        adjunto.setTipoArchivo(datos.getTipoArchivo());
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
}
