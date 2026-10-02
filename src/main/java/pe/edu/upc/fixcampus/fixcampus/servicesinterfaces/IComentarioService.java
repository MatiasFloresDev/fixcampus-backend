package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.dtos.ComentariosPorReporteDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTOInsert;
import java.util.List;

public interface IComentarioService {
    List<Comentario> listar();
    Comentario buscarPorId(Long id);
    Comentario registrar(ComentarioDTOInsert datos);
    Comentario actualizar(Long id, ComentarioDTOInsert datos);
    void eliminar(Long id);
    List<ComentariosPorReporteDTO> contarPorReporte();
    List<Comentario> buscarPorTexto(String texto);
}
