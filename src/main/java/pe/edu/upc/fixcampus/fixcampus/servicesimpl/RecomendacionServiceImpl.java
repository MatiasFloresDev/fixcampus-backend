package pe.edu.upc.fixcampus.fixcampus.servicesimpl;

import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.RecomendacionService;
import pe.edu.upc.fixcampus.fixcampus.repositories.*;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;
import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecomendacionServiceImpl implements RecomendacionService {
    private final RecomendacionRepository repository;
    private final ReporteRepository reporteRepository;

    public RecomendacionServiceImpl(RecomendacionRepository repository, ReporteRepository reporteRepository) {
        this.repository = repository;
        this.reporteRepository = reporteRepository;
    }

    public List<Recomendacion> listar() { return repository.findAll(); }
    public List<Recomendacion> buscarPorPrioridad(String prioridad) { return repository.findByPrioridadSugeridaIgnoreCase(prioridad); }
    public List<Recomendacion> buscarPorCategoria(String nombre) { return repository.findByCategoriaDelReporte(nombre); }

    public Recomendacion registrar(RecomendacionDTO datos) {
        if (repository.existsByReporte_IdReporte(datos.getReporteId())) {
            throw new IllegalArgumentException("Este reporte ya tiene una recomendación");
        }
        Recomendacion recomendacion = new Recomendacion();
        copiarDatos(recomendacion, datos);
        recomendacion.setFechaRecomendacion(LocalDateTime.now());
        return repository.save(recomendacion);
    }

    public Recomendacion actualizar(Long id, RecomendacionDTO datos) {
        Recomendacion recomendacion = buscarPorId(id);
        if (!recomendacion.getReporte().getIdReporte().equals(datos.getReporteId())
                && repository.existsByReporte_IdReporte(datos.getReporteId())) {
            throw new IllegalArgumentException("Este reporte ya tiene una recomendación");
        }
        copiarDatos(recomendacion, datos);
        return repository.save(recomendacion);
    }

    public Recomendacion buscarPorId(Long id) {
        Optional<Recomendacion> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Recomendacion no encontrado");
        }
        return encontrado.get();
    }

    private void copiarDatos(Recomendacion recomendacion, RecomendacionDTO datos) {
        Optional<Reporte> reporte =
                reporteRepository.findById(datos.getReporteId());
        if (reporte.isEmpty()) {
            throw new ResourceNotFoundException("Reporte no encontrado");
        }
        recomendacion.setReporte(reporte.get());
        recomendacion.setTituloSugerido(datos.getTituloSugerido());
        recomendacion.setResumen(datos.getResumen());
        recomendacion.setPrioridadSugerida(datos.getPrioridadSugerida());
        recomendacion.setJustificacion(datos.getJustificacion());
    }

    public void eliminar(Long id) { repository.delete(buscarPorId(id)); }
}
