package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.dtos.RegistroRequestDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTOInsert;
import java.util.List;

public interface IUsuarioService {
    List<Usuario> listar();
    long contarRegistrados();
    Usuario buscarPorId(Long id);
    Usuario crear(UsuarioDTOInsert datos);
    Usuario actualizar(Long id, UsuarioDTOInsert datos);
    Usuario registrar(RegistroRequestDTO datos);
    void eliminar(Long id);
    List<Usuario> buscarPorNombre(String nombre);
    List<Object[]> contarUsuariosPorRol();
}
