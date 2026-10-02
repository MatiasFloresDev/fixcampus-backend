package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTOInsert;
import java.util.List;

public interface IAdjuntoService {
    List<Adjunto> listar();
    List<Adjunto> buscarPorReporte(Long reporteId);
    Adjunto buscarPorId(Long id);
    Reporte buscarReporte(Long id);
    Adjunto registrar(AdjuntoDTOInsert datos);
    Adjunto actualizar(Long id, AdjuntoDTOInsert datos);
    void eliminar(Long id);
}
