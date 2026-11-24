package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;

import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.IAdjuntoRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IComentarioRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IRecomendacionRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IReporteRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IReporteService;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteServiceImplement implements IReporteService {

    private final IReporteRepository reporteRepository;
    private final IAdjuntoRepository adjuntoRepository;
    private final IComentarioRepository comentarioRepository;
    private final IRecomendacionRepository recomendacionRepository;

    public ReporteServiceImplement(
            IReporteRepository reporteRepository,
            IAdjuntoRepository adjuntoRepository,
            IComentarioRepository comentarioRepository,
            IRecomendacionRepository recomendacionRepository) {

        this.reporteRepository = reporteRepository;
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
        return reporteRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reporte no encontrado"));
    }

    @Override
    public Reporte registrar(Reporte datos) {
        datos.setTecnicoAsignado(null);
        datos.setPrioridad(null);
        datos.setEstado("PENDIENTE");
        datos.setFechaCreacion(LocalDateTime.now());
        datos.setFechaAsignacion(null);
        datos.setFechaResolucion(null);

        return reporteRepository.save(datos);
    }

    @Override
    public Reporte actualizar(Long id, Reporte datos) {
        Reporte actual = buscarPorId(id);

        actual.setCategoria(datos.getCategoria());
        actual.setUbicacion(datos.getUbicacion());
        actual.setTitulo(datos.getTitulo());
        actual.setDescripcion(datos.getDescripcion());
        actual.setDetalleUbicacion(datos.getDetalleUbicacion());

        if (actual.getFechaAsignacion() == null
                && actual.getTecnicoAsignado() == null
                && datos.getTecnicoAsignado() != null) {

            actual.setFechaAsignacion(LocalDateTime.now());
        }

        actual.setTecnicoAsignado(datos.getTecnicoAsignado());
        actual.setPrioridad(datos.getPrioridad());

        if ("RESUELTO".equalsIgnoreCase(datos.getEstado())
                && !"RESUELTO".equalsIgnoreCase(actual.getEstado())) {

            actual.setFechaResolucion(LocalDateTime.now());

        } else if (!"RESUELTO".equalsIgnoreCase(datos.getEstado())) {

            actual.setFechaResolucion(null);
        }

        actual.setEstado(datos.getEstado());

        return reporteRepository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Reporte reporte = buscarPorId(id);

        comentarioRepository.deleteAll(
                comentarioRepository.findByReporte_IdReporte(id));

        recomendacionRepository.deleteAll(
                recomendacionRepository.findByReporte_IdReporte(id));

        adjuntoRepository.deleteAll(
                adjuntoRepository.findByReporte_IdReporte(id));

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
    public List<Object[]> contarPorUsuarioYMes() {
        return reporteRepository.contarPorUsuarioYMes();
    }


    @Override
    public List<Reporte> buscarPorPrioridad(String prioridad) {
        return reporteRepository.findByPrioridadIgnoreCase(prioridad);
    }

    @Override
    public List<Object[]> contarPorCategoria() {
        return reporteRepository.contarPorCategoria();
    }
}