package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.dtos.EvidenciasPorUsuarioDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;

import java.util.List;

public interface IAdjuntoService {

    List<EvidenciasPorUsuarioDTO> contarEvidenciasPorUsuario(String correo);

    List<Adjunto> listar();

    List<Adjunto> buscarPorTipo(String tipo);

    List<Adjunto> buscarPorReporte(Long reporteId);

    Adjunto buscarPorId(Long id);

    Adjunto buscarPorUrl(String url);

    Adjunto registrar(Adjunto datos);

    Adjunto actualizar(Long id, Adjunto datos);

    void eliminar(Long id);
}