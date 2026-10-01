package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.dtos.EvidenciasPorUsuarioDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.IAdjuntoRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IAdjuntoService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdjuntoServiceImplement implements IAdjuntoService {

    private final IAdjuntoRepository adjuntoRepository;

    public AdjuntoServiceImplement(IAdjuntoRepository adjuntoRepository) {
        this.adjuntoRepository = adjuntoRepository;
    }

    @Override
    public List<EvidenciasPorUsuarioDTO> contarEvidenciasPorUsuario(
            String correo) {

        if (correo.isBlank()) {
            throw new IllegalArgumentException(
                    "El correo es obligatorio");
        }

        List<EvidenciasPorUsuarioDTO> lista = new ArrayList<>();

        for (Object[] fila :
                adjuntoRepository.contarEvidenciasPorUsuario(correo)) {

            lista.add(new EvidenciasPorUsuarioDTO(
                    ((Number) fila[0]).longValue(),
                    (String) fila[1],
                    (String) fila[2],
                    ((Number) fila[3]).longValue()
            ));
        }

        return lista;
    }

    @Override
    public List<Adjunto> listar() {
        return adjuntoRepository.findAll();
    }

    @Override
    public List<Adjunto> buscarPorTipo(String tipo) {
        return adjuntoRepository
                .findByTipoArchivoContainingIgnoreCase(tipo);
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
    public Adjunto buscarPorUrl(String url) {
        return adjuntoRepository.findByUrlArchivo(url)
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