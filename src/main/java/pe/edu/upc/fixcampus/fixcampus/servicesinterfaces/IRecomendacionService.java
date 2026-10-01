package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;

import java.util.List;

public interface IRecomendacionService {

    List<Recomendacion> listar();

    List<Recomendacion> buscarPorPrioridad(String prioridad);

    List<Recomendacion> buscarPorCategoria(String nombre);

    Recomendacion buscarPorId(Long id);

    Recomendacion registrar(Recomendacion datos);

    Recomendacion actualizar(Long id, Recomendacion datos);

    void eliminar(Long id);
}