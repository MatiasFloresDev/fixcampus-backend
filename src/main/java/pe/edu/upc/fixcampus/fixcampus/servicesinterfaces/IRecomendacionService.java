package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionesPorPrioridadDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;
import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionDTOInsert;
import java.util.List;
import java.util.Optional;

public interface IRecomendacionService {
    List<Recomendacion> listar();
    List<Recomendacion> buscarPorPrioridad(String prioridad);
    Recomendacion buscarPorId(Long id);
    Optional<Recomendacion> buscarPorReporte(Long reporteId);
    Recomendacion registrar(RecomendacionDTOInsert datos);
    Recomendacion actualizar(Long id, RecomendacionDTOInsert datos);
    void eliminar(Long id);
    List<RecomendacionesPorPrioridadDTO> contarPorPrioridadReporte();
}
