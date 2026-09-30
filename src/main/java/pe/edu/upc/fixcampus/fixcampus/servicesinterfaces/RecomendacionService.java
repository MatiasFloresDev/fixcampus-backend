package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;
import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionDTO;
import java.util.List;

public interface RecomendacionService {
    List<Recomendacion> listar();
    List<Recomendacion> buscarPorPrioridad(String prioridad);
    List<Recomendacion> buscarPorCategoria(String nombre);
    Recomendacion buscarPorId(Long id);
    Recomendacion registrar(RecomendacionDTO datos);
    Recomendacion actualizar(Long id, RecomendacionDTO datos);
    void eliminar(Long id);
}
