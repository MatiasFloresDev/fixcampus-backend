package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import java.util.List;

public interface IUbicacionService {
    List<Ubicacion> listar();
    List<Ubicacion> buscarPorCampus(String campus);
    Ubicacion buscarPorId(Long id);
    Ubicacion registrar(Ubicacion datos);
    Ubicacion actualizar(Long id, Ubicacion datos);
    void eliminar(Long id);
}
