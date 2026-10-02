package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IAdjuntoService;
import pe.edu.upc.fixcampus.fixcampus.repositories.*;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdjuntoServiceImplement implements IAdjuntoService {
    private final IAdjuntoRepository repository;
    private final IReporteRepository reporteRepository;

    public AdjuntoServiceImplement(IAdjuntoRepository repository, IReporteRepository reporteRepository) {
        this.repository = repository;
        this.reporteRepository = reporteRepository;
    }

    public void eliminar(Long id) {
        Adjunto adjunto = buscarPorId(id);
        repository.delete(adjunto);
    }

    public List<Adjunto> listar() { return repository.findAll(); }
    public List<Adjunto> buscarPorReporte(Long reporteId) { return repository.findByReporte_IdReporte(reporteId); }


    public Adjunto registrar(AdjuntoDTO datos) {
        Adjunto adjunto = new Adjunto();
        copiarDatos(adjunto, datos);
        adjunto.setFechaSubida(LocalDateTime.now());
        return repository.save(adjunto);
    }

    public Adjunto actualizar(Long id, AdjuntoDTO datos) {
        Adjunto adjunto = buscarPorId(id);
        copiarDatos(adjunto, datos);
        return repository.save(adjunto);
    }

    public Adjunto buscarPorId(Long id) {
        Optional<Adjunto> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Adjunto no encontrado");
        }
        return encontrado.get();
    }

    private void copiarDatos(Adjunto adjunto, AdjuntoDTO datos) {
        adjunto.setReporte(buscarReporte(datos.getReporteId()));
        adjunto.setNombreArchivo(datos.getNombreArchivo());
        adjunto.setUrlArchivo(datos.getUrlArchivo());
        adjunto.setTipoArchivo(datos.getTipoArchivo());
    }

    public Reporte buscarReporte(Long id) {
        Optional<Reporte> encontrado = reporteRepository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Reporte no encontrado");
        }
        return encontrado.get();
    }

}
