package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentariosPorReporteDTO;
import java.util.ArrayList;

public interface ComentarioService {
    List<Comentario> listar();
    List<Comentario> buscarPorReporte(Long reporteId);
    List<ComentariosPorReporteDTO> contarPorReporteYCorreo(String correo);
    Comentario buscarPorId(Long id);
    Comentario registrar(ComentarioDTO datos);
    Comentario actualizar(Long id, ComentarioDTO datos);
    void eliminar(Long id);
}
