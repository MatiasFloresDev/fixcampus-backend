package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import java.util.ArrayList;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentariosPorReporteDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IComentarioService;
import pe.edu.upc.fixcampus.fixcampus.repositories.*;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTOInsert;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComentarioServiceImplement implements IComentarioService {
    private final IComentarioRepository repository;
    private final IReporteRepository reporteRepository;
    private final IUsuarioRepository usuarioRepository;

    public ComentarioServiceImplement(IComentarioRepository repository, IReporteRepository reporteRepository, IUsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Comentario> listar() { return repository.findAll(); }

    public Comentario registrar(ComentarioDTOInsert datos) {
        Comentario comentario = new Comentario();
        copiarDatos(comentario, datos);
        comentario.setFechaComentario(LocalDateTime.now());
        return repository.save(comentario);
    }

    public Comentario actualizar(Long id, ComentarioDTOInsert datos) {
        Comentario comentario = buscarPorId(id);
        copiarDatos(comentario, datos);
        return repository.save(comentario);
    }

    public Comentario buscarPorId(Long id) {
        Optional<Comentario> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Comentario no encontrado");
        }
        return encontrado.get();
    }

    private void copiarDatos(Comentario comentario, ComentarioDTOInsert datos) {
        Optional<Reporte> reporte =
                reporteRepository.findById(datos.getReporteId());
        if (reporte.isEmpty()) {
            throw new ResourceNotFoundException("Reporte no encontrado");
        }
        comentario.setReporte(reporte.get());

        Optional<Usuario> usuario =
                usuarioRepository.findById(datos.getUsuarioId());
        if (usuario.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        comentario.setUsuario(usuario.get());
        comentario.setTextoComentario(datos.getTextoComentario());
    }

    public void eliminar(Long id) { repository.delete(buscarPorId(id)); }
    public List<ComentariosPorReporteDTO> contarPorReporte() {
        List<ComentariosPorReporteDTO> lista = new ArrayList<>();
        for (Object[] fila : repository.contarPorReporte()) {
            lista.add(new ComentariosPorReporteDTO(((Number) fila[0]).longValue(), (String) fila[1], ((Number) fila[2]).longValue()));
        }
        return lista;
    }
    public List<Comentario> buscarPorTexto(String texto) { return repository.findByTextoComentarioContainingIgnoreCase(texto); }
}
