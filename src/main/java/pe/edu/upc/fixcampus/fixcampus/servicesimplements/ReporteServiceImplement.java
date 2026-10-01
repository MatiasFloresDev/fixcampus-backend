package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import java.time.LocalDate;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCategoriaDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.ICategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IAdjuntoRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IComentarioRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IRecomendacionRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUbicacionRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IReporteRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUsuarioRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IReporteService;

import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ReporteServiceImplement implements IReporteService {
    @Override
    public List<IncidenciasPorCategoriaDTO> contarPorCategoriaEntreFechas(
            LocalDate desde, LocalDate hasta) {
        if (!hasta.isAfter(desde)) {
            throw new IllegalArgumentException("La fecha hasta debe ser posterior a desde");
        }
        List<IncidenciasPorCategoriaDTO> lista = new ArrayList<>();
        for (Object[] fila : reporteRepository.contarPorCategoriaEntreFechas(desde.atStartOfDay(), hasta.atStartOfDay())) {
            lista.add(new IncidenciasPorCategoriaDTO(
                    ((Number) fila[0]).longValue(), (String) fila[1], ((Number) fila[2]).longValue()));
        }
        return lista;
    }

    private final IReporteRepository reporteRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ICategoriaRepository categoriaRepository;
    private final IUbicacionRepository ubicacionRepository;
    private final IAdjuntoRepository adjuntoRepository;
    private final IComentarioRepository comentarioRepository;
    private final IRecomendacionRepository recomendacionRepository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    public ReporteServiceImplement(IReporteRepository reporteRepository,
                                   IUsuarioRepository usuarioRepository,
                                   ICategoriaRepository categoriaRepository,
                                   IUbicacionRepository ubicacionRepository,
                                   IAdjuntoRepository adjuntoRepository,
                                   IComentarioRepository comentarioRepository,
                                   IRecomendacionRepository recomendacionRepository) {
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.adjuntoRepository = adjuntoRepository;
        this.comentarioRepository = comentarioRepository;
        this.recomendacionRepository = recomendacionRepository;
    }

    @Override
    public List<Reporte> listar() {

        return reporteRepository.findAll();
    }

    @Override
    public Reporte buscarPorId(Long id) {
        Optional<Reporte> encontrado = reporteRepository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Reporte no encontrado");
        }
        return encontrado.get();
    }

    @Override
    public Reporte registrar(ReporteDTOInsert dto) {
        Reporte reporte = new Reporte();
        dto.setEstado("ABIERTO");
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
    @Transactional
    public void eliminar(Long id) {
        Reporte reporte = buscarPorId(id);
        Path directory = Paths.get(uploadDir).toAbsolutePath().normalize();
        List<Adjunto> adjuntos =
                adjuntoRepository.findByReporte_IdReporte(id);
        // Primero se eliminan los hijos para respetar las llaves foráneas.
        comentarioRepository.deleteAll(comentarioRepository.findByReporte_IdReporte(id));
        recomendacionRepository.deleteAll(recomendacionRepository.findByReporte_IdReporte(id));
        adjuntoRepository.deleteAll(adjuntos);
        reporteRepository.delete(reporte);
        // Fuerza a comprobar las restricciones antes de borrar los archivos físicos.
        reporteRepository.flush();
        for (Adjunto adjunto : adjuntos) {
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
        }
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
        List<IncidenciasPorMesDTO> lista = new ArrayList<>();
        for (Object[] fila : reporteRepository.contarPorUsuarioYMes()) {
            lista.add(new IncidenciasPorMesDTO(((Number) fila[0]).longValue(),
                    (String) fila[1], (String) fila[2], ((Number) fila[3]).intValue(),
                    ((Number) fila[4]).intValue(), ((Number) fila[5]).longValue()));
        }
        return lista;
    }

    @Override
    public List<IncidenciasPorCampusDTO> contarPorCampusYEstado(String estado) {
        List<IncidenciasPorCampusDTO> lista = new ArrayList<>();
        for (Object[] fila : reporteRepository.contarPorCampusYEstado(estado)) {
            lista.add(new IncidenciasPorCampusDTO((String) fila[0], ((Number) fila[1]).longValue()));
        }
        return lista;
    }

    @Override
    public List<Reporte> buscarPorPrioridad(String prioridad) {
        return reporteRepository.findByPrioridad(prioridad);
    }

    @Override
    public List<Reporte> buscarPorCampus(String campus) {
        return reporteRepository.findByCampus(campus);
    }

    private void copiarDatos(Reporte reporte, ReporteDTOInsert dto) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(dto.getUsuarioReportanteId());
        if (usuarioEncontrado.isEmpty()) {
            throw new ResourceNotFoundException("Usuario reportante no encontrado");
        }
        Usuario usuario = usuarioEncontrado.get();

        Optional<Categoria> categoriaEncontrada = categoriaRepository.findById(dto.getCategoriaId());
        if (categoriaEncontrada.isEmpty()) {
            throw new ResourceNotFoundException("Categoría no encontrada");
        }
        Categoria categoria = categoriaEncontrada.get();

        Optional<Ubicacion> ubicacionEncontrada = ubicacionRepository.findById(dto.getUbicacionId());
        if (ubicacionEncontrada.isEmpty()) {
            throw new ResourceNotFoundException("Ubicación no encontrada");
        }
        Ubicacion ubicacion = ubicacionEncontrada.get();

        reporte.setUsuarioReportante(usuario);
        reporte.setCategoria(categoria);
        reporte.setUbicacion(ubicacion);
        Usuario tecnico = null;
        if (dto.getTecnicoAsignadoId() != null) {
            Optional<Usuario> tecnicoEncontrado = usuarioRepository.findById(dto.getTecnicoAsignadoId());
            if (tecnicoEncontrado.isEmpty()) {
                throw new ResourceNotFoundException("Técnico no encontrado");
            }
            tecnico = tecnicoEncontrado.get();
        }
        if (tecnico != null && (reporte.getTecnicoAsignado() == null
                || !tecnico.getIdUsuario().equals(reporte.getTecnicoAsignado().getIdUsuario()))) {
            reporte.setFechaAsignacion(LocalDateTime.now());
        } else if (tecnico == null) {
            reporte.setFechaAsignacion(null);
        }
        reporte.setTecnicoAsignado(tecnico);
        reporte.setTitulo(dto.getTitulo());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setDetalleUbicacion(dto.getDetalleUbicacion());
        reporte.setPrioridad(dto.getPrioridad());
        if ("RESUELTO".equalsIgnoreCase(dto.getEstado())) {
            if (reporte.getFechaResolucion() == null) {
                reporte.setFechaResolucion(LocalDateTime.now());
            }
        } else {
            reporte.setFechaResolucion(null);
        }
        reporte.setEstado(dto.getEstado());
    }
}
