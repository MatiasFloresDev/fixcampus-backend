package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;

import java.util.List;

public interface IUsuarioService {

    List<Usuario> listar();

    Usuario buscarPorId(Long id);

    Usuario buscarPorCorreo(String correo);

    Usuario registrar(Usuario datos);

    Usuario actualizar(Long id, Usuario datos);

    void eliminar(Long id);

    List<Object[]> contarUsuariosPorRol();

    List<Usuario> buscarPorNombre(String nombre);
}
