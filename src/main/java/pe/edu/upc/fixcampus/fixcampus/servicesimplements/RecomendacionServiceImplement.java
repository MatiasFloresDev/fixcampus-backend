package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.IRecomendacionRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IRecomendacionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecomendacionServiceImplement implements IRecomendacionService {

    private final IRecomendacionRepository recomendacionRepository;

    public RecomendacionServiceImplement(
            IRecomendacionRepository recomendacionRepository) {
        this.recomendacionRepository = recomendacionRepository;
    }

    @Override
    public List<Recomendacion> listar() {
        return recomendacionRepository.findAll();
    }

    @Override
    public List<Recomendacion> buscarPorPrioridad(String prioridad) {
        return recomendacionRepository
                .findByPrioridadSugeridaIgnoreCase(prioridad);
    }

    @Override
    public List<Recomendacion> buscarPorCategoria(String nombre) {
        return recomendacionRepository.findByCategoriaDelReporte(nombre);
    }

    @Override
    public Recomendacion buscarPorId(Long id) {
        return recomendacionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Recomendación no encontrada"));
    }

    @Override
    public Recomendacion registrar(Recomendacion datos) {

        if (recomendacionRepository.existsByReporte_IdReporte(
                datos.getReporte().getIdReporte())) {

            throw new IllegalArgumentException(
                    "Este reporte ya tiene una recomendación");
        }

        datos.setFechaRecomendacion(LocalDateTime.now());

        return recomendacionRepository.save(datos);
    }

    @Override
    public Recomendacion actualizar(Long id, Recomendacion datos) {
        Recomendacion actual = buscarPorId(id);

        actual.setTituloSugerido(datos.getTituloSugerido());
        actual.setResumen(datos.getResumen());
        actual.setPrioridadSugerida(datos.getPrioridadSugerida());
        actual.setJustificacion(datos.getJustificacion());

        return recomendacionRepository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Recomendacion actual = buscarPorId(id);
        recomendacionRepository.delete(actual);
    }
}