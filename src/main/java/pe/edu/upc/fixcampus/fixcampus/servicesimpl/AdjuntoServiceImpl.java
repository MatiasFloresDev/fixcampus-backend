package pe.edu.upc.fixcampus.fixcampus.servicesimpl;

import java.util.ArrayList;
import pe.edu.upc.fixcampus.fixcampus.dtos.EvidenciasPorUsuarioDTO;
import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.AdjuntoService;
import pe.edu.upc.fixcampus.fixcampus.repositories.*;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdjuntoServiceImpl implements AdjuntoService {
    public List<EvidenciasPorUsuarioDTO> contarEvidenciasPorUsuario(String correo) {
        if (correo.isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        List<EvidenciasPorUsuarioDTO> lista = new ArrayList<>();
        for (Object[] fila : repository.contarEvidenciasPorUsuario(correo)) {
            lista.add(new EvidenciasPorUsuarioDTO(
                    ((Number) fila[0]).longValue(), (String) fila[1], (String) fila[2],
                    ((Number) fila[3]).longValue()));
        }
        return lista;
    }
    private final AdjuntoRepository repository;
    private final ReporteRepository reporteRepository;

    public AdjuntoServiceImpl(AdjuntoRepository repository, ReporteRepository reporteRepository) {
        this.repository = repository;
        this.reporteRepository = reporteRepository;
    }

    public void eliminar(Long id) {
        Adjunto adjunto = buscarPorId(id);
        repository.delete(adjunto);
    }

    public List<Adjunto> listar() { return repository.findAll(); }
    public List<Adjunto> buscarPorTipo(String tipo) { return repository.findByTipoArchivoContainingIgnoreCase(tipo); }
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
