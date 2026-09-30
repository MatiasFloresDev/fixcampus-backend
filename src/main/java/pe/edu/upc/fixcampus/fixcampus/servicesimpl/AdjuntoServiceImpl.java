package pe.edu.upc.fixcampus.fixcampus.servicesimpl;

import java.util.ArrayList;
import pe.edu.upc.fixcampus.fixcampus.dtos.EvidenciasPorUsuarioDTO;
import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.AdjuntoService;
import pe.edu.upc.fixcampus.fixcampus.repositories.*;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Service
public class AdjuntoServiceImpl implements AdjuntoService {
    public List<EvidenciasPorUsuarioDTO> contarEvidenciasPorUsuario(String correo) {
        if (correo.isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        List<EvidenciasPorUsuarioDTO> lista = new ArrayList<>();
        for (Object[] fila : repository.contarEvidenciasPorUsuario(correo)) {
            lista.add(new EvidenciasPorUsuarioDTO(
                    ((Number) fila[0]).longValue(), (String) fila[1], (String) fila[2],
                    ((Number) fila[3]).longValue()));
        }
        return lista;
    }
    private final AdjuntoRepository repository;
    private final ReporteRepository reporteRepository;
    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    public AdjuntoServiceImpl(AdjuntoRepository repository, ReporteRepository reporteRepository) {
        this.repository = repository;
        this.reporteRepository = reporteRepository;
    }

    public List<Adjunto> listar() { return repository.findAll(); }
    public List<Adjunto> buscarPorTipo(String tipo) { return repository.findByTipoArchivoContainingIgnoreCase(tipo); }
    public List<Adjunto> buscarPorReporte(Long reporteId) { return repository.findByReporte_IdReporte(reporteId); }
    public Adjunto buscarPorUrl(String url) {
        Optional<Adjunto> encontrado = repository.findByUrlArchivo(url);
        if (encontrado.isEmpty()) { throw new ResourceNotFoundException("Archivo no encontrado"); }
        return encontrado.get();
    }
    public Path rutaArchivo(String fileName) {
        Path directorio = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path archivo = directorio.resolve(fileName).normalize();
        if (!archivo.startsWith(directorio) || !Files.exists(archivo)) {
            throw new ResourceNotFoundException("Archivo no encontrado");
        }
        return archivo;
    }
    public void eliminar(Long id) {
        Adjunto adjunto = buscarPorId(id);
        repository.delete(adjunto);
        String prefijo = "/api/attachments/files/";
        if (adjunto.getUrlArchivo().startsWith(prefijo)) {
            Path directorio = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path archivo = directorio.resolve(adjunto.getUrlArchivo().substring(prefijo.length())).normalize();
            if (archivo.startsWith(directorio)) {
                try { Files.deleteIfExists(archivo); }
                catch (IOException ex) { System.err.println("No se pudo eliminar el archivo: " + archivo); }
            }
        }
    }

    public Adjunto subir(Long reporteId, MultipartFile file) throws IOException {
        Reporte reporte = buscarReporte(reporteId);
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("El archivo no puede superar los 5 MB");
        }
        String contentType;
        if (file.getContentType() == null) {
            contentType = "";
        } else {
            contentType = file.getContentType().toLowerCase(Locale.ROOT);
        }
        Set<String> tiposPermitidos = Set.of("image/jpeg", "image/png", "application/pdf");
        if (!tiposPermitidos.contains(contentType)) {
            throw new IllegalArgumentException("Solo se permiten archivos JPG, PNG o PDF");
        }
        if (repository.countByReporte_IdReporte(reporteId) >= 3) {
            throw new IllegalArgumentException("Un reporte puede tener como máximo 3 evidencias");
        }

        String original;
        if (file.getOriginalFilename() == null) {
            original = "evidencia";
        } else {
            original = Paths.get(file.getOriginalFilename()).getFileName().toString();
        }
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
        return repository.save(adjunto);
    }

    public Adjunto registrar(AdjuntoDTO datos) {
        Adjunto adjunto = new Adjunto();
        copiarDatos(adjunto, datos);
        adjunto.setFechaSubida(LocalDateTime.now());
        return repository.save(adjunto);
    }

    public Adjunto actualizar(Long id, AdjuntoDTO datos) {
        Adjunto adjunto = buscarPorId(id);
        copiarDatos(adjunto, datos);
        return repository.save(adjunto);
    }

    public Adjunto buscarPorId(Long id) {
        Optional<Adjunto> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Adjunto no encontrado");
        }
        return encontrado.get();
    }

    private void copiarDatos(Adjunto adjunto, AdjuntoDTO datos) {
        adjunto.setReporte(buscarReporte(datos.getReporteId()));
        adjunto.setNombreArchivo(datos.getNombreArchivo());
        adjunto.setUrlArchivo(datos.getUrlArchivo());
        adjunto.setTipoArchivo(datos.getTipoArchivo());
    }

    public Reporte buscarReporte(Long id) {
        Optional<Reporte> encontrado = reporteRepository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Reporte no encontrado");
        }
        return encontrado.get();
    }

    private String extension(String original, String contentType) {
        String lower = original.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".pdf")) {
            return lower.substring(lower.lastIndexOf('.'));
        }
        if ("image/jpeg".equals(contentType)) {
            return ".jpg";
        }
        if ("image/png".equals(contentType)) {
            return ".png";
        }
        return ".pdf";
    }
}
