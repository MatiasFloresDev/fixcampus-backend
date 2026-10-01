package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentariosPorReporteDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.IComentarioRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IComentarioService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComentarioServiceImplement implements IComentarioService {

    private final IComentarioRepository comentarioRepository;

    public ComentarioServiceImplement(IComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    @Override
    public List<Comentario> listar() {
        return comentarioRepository.findAll();
    }

    @Override
    public List<Comentario> buscarPorReporte(Long reporteId) {
        return comentarioRepository.findByReporte_IdReporte(reporteId);
    }

    @Override
    public List<ComentariosPorReporteDTO> contarPorReporteYCorreo(String correo) {
        List<ComentariosPorReporteDTO> lista = new ArrayList<>();

        for (Object[] fila : comentarioRepository.contarPorReporteYCorreo(correo)) {
            lista.add(new ComentariosPorReporteDTO(
                    ((Number) fila[0]).longValue(),
                    (String) fila[1],
                    ((Number) fila[2]).longValue()
            ));
        }

        return lista;
    }

    @Override
    public Comentario buscarPorId(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comentario no encontrado"));
    }

    @Override
    public Comentario registrar(Comentario datos) {
        datos.setFechaComentario(LocalDateTime.now());
        return comentarioRepository.save(datos);
    }

    @Override
    public Comentario actualizar(Long id, Comentario datos) {
        Comentario actual = buscarPorId(id);

        actual.setTextoComentario(datos.getTextoComentario());

        return comentarioRepository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Comentario actual = buscarPorId(id);
        comentarioRepository.delete(actual);
    }
}