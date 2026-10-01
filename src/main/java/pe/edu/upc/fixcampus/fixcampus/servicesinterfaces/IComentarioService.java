package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.dtos.ComentariosPorReporteDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;

import java.util.List;

public interface IComentarioService {

    List<Comentario> listar();

    List<Comentario> buscarPorReporte(Long reporteId);

    List<ComentariosPorReporteDTO> contarPorReporteYCorreo(String correo);

    Comentario buscarPorId(Long id);

    Comentario registrar(Comentario datos);

    Comentario actualizar(Long id, Comentario datos);

    void eliminar(Long id);
}