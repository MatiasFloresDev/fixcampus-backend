package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.dtos.EvidenciasPorUsuarioDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTO;
import java.util.List;

public interface IAdjuntoService {
    List<EvidenciasPorUsuarioDTO> contarEvidenciasPorUsuario(String correo);
    List<Adjunto> listar();
    List<Adjunto> buscarPorTipo(String tipo);
    List<Adjunto> buscarPorReporte(Long reporteId);
    Adjunto buscarPorId(Long id);
    Reporte buscarReporte(Long id);
    Adjunto registrar(AdjuntoDTO datos);
    Adjunto actualizar(Long id, AdjuntoDTO datos);
    void eliminar(Long id);
}
