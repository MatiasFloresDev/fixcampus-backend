package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;

import java.util.List;

public interface IRecomendacionService {

    List<Recomendacion> listar();

    List<Recomendacion> buscarPorPrioridad(String prioridad);

    Recomendacion buscarPorId(Long id);

    Recomendacion registrar(Recomendacion datos);

    Recomendacion actualizar(Long id, Recomendacion datos);

    void eliminar(Long id);

    List<Object[]> contarPorPrioridadDelReporte();
}
