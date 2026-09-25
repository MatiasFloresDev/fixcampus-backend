package pe.edu.upc.fixcampus.fixcampus.servicesimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.CategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.AdjuntoRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UbicacionRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.ReporteRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.ReporteService;

import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final UbicacionRepository ubicacionRepository;
    private final AdjuntoRepository adjuntoRepository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    public ReporteServiceImpl(ReporteRepository reporteRepository,
                             UsuarioRepository usuarioRepository,
                             CategoriaRepository categoriaRepository,
                             UbicacionRepository ubicacionRepository,
                             AdjuntoRepository adjuntoRepository) {
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.adjuntoRepository = adjuntoRepository;
    }

    @Override
    public List<Reporte> listar() {

        return reporteRepository.findAll();
    }

    @Override
    public Reporte buscarPorId(Long id) {
        return reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado"));
    }

    @Override
    public Reporte registrar(ReporteDTOInsert dto) {
        Reporte reporte = new Reporte();
        copiarDatos(reporte, dto);
        reporte.setFechaCreacion(LocalDateTime.now());
        return reporteRepository.save(reporte);
    }

    @Override
    public Reporte actualizar(Long id, ReporteDTOInsert dto) {
        Reporte reporte = buscarPorId(id);
        copiarDatos(reporte, dto);
        return reporteRepository.save(reporte);
    }

    @Override
    public void eliminar(Long id) {
        Reporte reporte = buscarPorId(id);
        Path directory = Paths.get(uploadDir).toAbsolutePath().normalize();
        adjuntoRepository.findByReporte_IdReporte(id).forEach(adjunto -> {
            String marker = "/api/attachments/files/";
            String url = adjunto.getUrlArchivo();
            if (url != null && url.startsWith(marker)) {
                Path archivo = directory.resolve(url.substring(marker.length())).normalize();
                if (archivo.startsWith(directory)) {
                    try {
                        Files.deleteIfExists(archivo);
                    } catch (java.io.IOException ignored) {
                        // El registro se elimina aunque el archivo físico requiera limpieza posterior.
                    }
                }
            }
        });
        adjuntoRepository.deleteAll(adjuntoRepository.findByReporte_IdReporte(id));
        reporteRepository.delete(reporte);
    }

    @Override
    public List<Reporte> buscarPorEstado(String estado) {

        return reporteRepository.findByEstadoIgnoreCase(estado);
    }

    @Override
    public List<Reporte> buscarPorCategoria(String nombreCategoria) {
        return reporteRepository.findByNombreCategoria(nombreCategoria);
    }

    @Override
    public List<Reporte> buscarPorCorreoReportante(String correo) {
        return reporteRepository.findByCorreoReportante(correo);
    }

    @Override
    public List<IncidenciasPorMesDTO> contarPorUsuarioYMes() {
        return reporteRepository.contarPorUsuarioYMes();
    }

    @Override
    public List<IncidenciasPorCampusDTO> contarPorCampusYEstado(String estado) {
        return reporteRepository.contarPorCampusYEstado(estado);
    }

    private void copiarDatos(Reporte reporte, ReporteDTOInsert dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioReportanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario reportante no encontrado"));
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        Ubicacion ubicacion = ubicacionRepository.findById(dto.getUbicacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada"));

        reporte.setUsuarioReportante(usuario);
        reporte.setCategoria(categoria);
        reporte.setUbicacion(ubicacion);
        reporte.setTecnicoAsignado(dto.getTecnicoAsignadoId() == null ? null
                : usuarioRepository.findById(dto.getTecnicoAsignadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Técnico no encontrado")));
        reporte.setTitulo(dto.getTitulo());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setDetalleUbicacion(dto.getDetalleUbicacion());
        reporte.setPrioridad(dto.getPrioridad());
        reporte.setEstado(dto.getEstado());
    }
}
