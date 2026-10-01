package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;

import java.util.List;

public interface IUsuarioService {

    List<Usuario> listar();

    List<Usuario> buscarPorEstado(String estado);

    long contarRegistrados();

    Usuario buscarPorId(Long id);

    Usuario registrar(Usuario datos);

    Usuario actualizar(Long id, Usuario datos);

    void eliminar(Long id);
}