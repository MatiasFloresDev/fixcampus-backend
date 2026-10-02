package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTO;
import java.util.List;

public interface IComentarioService {
    List<Comentario> listar();
    Comentario buscarPorId(Long id);
    Comentario registrar(ComentarioDTO datos);
    Comentario actualizar(Long id, ComentarioDTO datos);
    void eliminar(Long id);
}
