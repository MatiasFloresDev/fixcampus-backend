package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;

import java.util.List;

public interface IAdjuntoService {

    List<Adjunto> listar();

    List<Adjunto> buscarPorReporte(Long reporteId);

    Adjunto buscarPorId(Long id);

    Adjunto registrar(Adjunto datos);

    Adjunto actualizar(Long id, Adjunto datos);

    void eliminar(Long id);
}