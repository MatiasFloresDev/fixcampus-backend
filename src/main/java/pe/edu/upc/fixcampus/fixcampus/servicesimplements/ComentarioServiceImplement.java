package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IComentarioService;
import pe.edu.upc.fixcampus.fixcampus.repositories.*;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentariosPorReporteDTO;
import java.util.ArrayList;

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
    public List<Comentario> buscarPorReporte(Long reporteId) { return repository.findByReporte_IdReporte(reporteId); }
    public List<ComentariosPorReporteDTO> contarPorReporteYCorreo(String correo) {
        List<ComentariosPorReporteDTO> lista = new ArrayList<>();
        for (Object[] fila : repository.contarPorReporteYCorreo(correo)) {
            lista.add(new ComentariosPorReporteDTO(((Number) fila[0]).longValue(),
                    (String) fila[1], ((Number) fila[2]).longValue()));
        }
        return lista;
    }

    public Comentario registrar(ComentarioDTO datos) {
        Comentario comentario = new Comentario();
        copiarDatos(comentario, datos);
        comentario.setFechaComentario(LocalDateTime.now());
        return repository.save(comentario);
    }

    public Comentario actualizar(Long id, ComentarioDTO datos) {
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

    private void copiarDatos(Comentario comentario, ComentarioDTO datos) {
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
}
