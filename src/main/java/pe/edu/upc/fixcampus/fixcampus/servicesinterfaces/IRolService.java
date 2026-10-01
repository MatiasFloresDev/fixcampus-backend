package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import java.util.List;

public interface IRolService {
    List<Rol> listar();
    List<Rol> buscarPorNombre(String nombre);
    Rol buscarPorNombreExacto(String nombre);
    Rol buscarPorId(Long id);
    Rol registrar(Rol datos);
    Rol actualizar(Long id, Rol datos);
    void eliminar(Long id);
}
