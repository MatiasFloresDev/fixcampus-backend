package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.IAdjuntoRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IAdjuntoService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdjuntoServiceImplement implements IAdjuntoService {

    private final IAdjuntoRepository adjuntoRepository;

    public AdjuntoServiceImplement(IAdjuntoRepository adjuntoRepository) {
        this.adjuntoRepository = adjuntoRepository;
    }

    @Override
    public List<Adjunto> listar() {
        return adjuntoRepository.findAll();
    }

    @Override
    public List<Adjunto> buscarPorReporte(Long reporteId) {
        return adjuntoRepository.findByReporte_IdReporte(reporteId);
    }

    @Override
    public Adjunto buscarPorId(Long id) {
        return adjuntoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Adjunto no encontrado"));
    }

    @Override
    public Adjunto registrar(Adjunto datos) {

        if (adjuntoRepository.countByReporte_IdReporte(
                datos.getReporte().getIdReporte()) >= 3) {

            throw new IllegalArgumentException(
                    "Un reporte puede tener como máximo 3 evidencias");
        }

        datos.setFechaSubida(LocalDateTime.now());

        return adjuntoRepository.save(datos);
    }

    @Override
    public Adjunto actualizar(Long id, Adjunto datos) {
        Adjunto actual = buscarPorId(id);

        actual.setNombreArchivo(datos.getNombreArchivo());
        actual.setUrlArchivo(datos.getUrlArchivo());
        actual.setTipoArchivo(datos.getTipoArchivo());

        return adjuntoRepository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Adjunto actual = buscarPorId(id);
        adjuntoRepository.delete(actual);
    }
}
