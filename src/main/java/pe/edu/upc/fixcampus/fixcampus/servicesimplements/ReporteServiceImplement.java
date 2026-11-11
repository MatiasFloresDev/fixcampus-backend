package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.ICategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUbicacionRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IReporteRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUsuarioRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IReporteService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ReporteServiceImplement implements IReporteService {

    private final IReporteRepository reporteRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ICategoriaRepository categoriaRepository;
    private final IUbicacionRepository ubicacionRepository;


    public ReporteServiceImplement(IReporteRepository reporteRepository,
                             IUsuarioRepository usuarioRepository,
                             ICategoriaRepository categoriaRepository,
                             IUbicacionRepository ubicacionRepository) {
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;
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
    @Transactional
    public Reporte actualizar(Long id, ReporteDTOInsert dto, Long idUsuario, boolean administrador) {
        Reporte reporte = reporteRepository.findByIdParaActualizar(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado"));
        Long idReportante = reporte.getUsuarioReportante().getIdUsuario();
        if (!administrador && !idReportante.equals(idUsuario)) {
            throw new AccessDeniedException("Solo puedes editar tus propios reportes");
        }
        dto.setUsuarioReportanteId(idReportante);
        if (!administrador) {
            dto.setPrioridad(reporte.getPrioridad());
            dto.setEstado(reporte.getEstado());
            Usuario tecnico = reporte.getTecnicoAsignado();
            dto.setTecnicoAsignadoId(tecnico == null ? null : tecnico.getIdUsuario());
        }
        copiarDatos(reporte, dto);
        return reporteRepository.save(reporte);
    }

    @Override
    public void eliminar(Long id) {
        Reporte reporte = buscarPorId(id);
        // Si tiene datos relacionados, la FK impide borrarlo hasta retirarlos.
        reporteRepository.delete(reporte);
    }

    @Override
    public List<Reporte> buscarPorEstado(String estado) {

        return reporteRepository.findByEstadoIgnoreCase(estado);
    }


    @Override
    public List<Reporte> buscarPorReportante(Long idUsuario) {
        return reporteRepository.findByUsuarioReportante_IdUsuarioOrderByFechaCreacionDescIdReporteDesc(idUsuario);
    }

    @Override
    public List<Reporte> buscarPropiosPorEstado(Long idUsuario, String estado) {
        return reporteRepository.findByUsuarioReportante_IdUsuarioAndEstadoIgnoreCase(idUsuario, estado);
    }

    @Override
    public List<Reporte> buscarPropiosPorPrioridad(Long idUsuario, String prioridad) {
        return reporteRepository.findByUsuarioReportante_IdUsuarioAndPrioridadIgnoreCase(idUsuario, prioridad);
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
    public List<IncidenciasPorCampusDTO> contarPorCampusYEstado(String estado) {
        List<IncidenciasPorCampusDTO> lista = new ArrayList<>();
        for (Object[] fila : reporteRepository.contarPorCampusYEstado(estado)) {
            lista.add(new IncidenciasPorCampusDTO((String) fila[0], ((Number) fila[1]).longValue()));
        }
        return lista;
    }
}
