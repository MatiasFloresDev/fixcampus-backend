package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import java.util.List;
import java.util.Optional;

public interface UbicacionService {
    List<Ubicacion> listar();
    List<Ubicacion> buscarPorCampus(String campus);
    Ubicacion buscarPorId(Long id);
    Ubicacion registrar(Ubicacion datos);
    Ubicacion actualizar(Long id, Ubicacion datos);
    void eliminar(Long id);
}
