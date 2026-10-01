package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUsuarioRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUsuarioService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioServiceImplement implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;

    public UsuarioServiceImplement(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> buscarPorEstado(String estado) {
        return usuarioRepository.findByEstadoIgnoreCase(estado);
    }

    @Override
    public long contarRegistrados() {
        return usuarioRepository.count();
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"));
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"));
    }

    @Override
    public Usuario registrar(Usuario datos) {

        if (usuarioRepository.existsByCorreo(datos.getCorreo())) {
            throw new IllegalArgumentException(
                    "El correo ya está registrado");
        }

        datos.setEstado("ACTIVO");
        datos.setFechaRegistro(LocalDateTime.now());

        return usuarioRepository.save(datos);
    }

    @Override
    public Usuario actualizar(Long id, Usuario datos) {
        Usuario actual = buscarPorId(id);

        if (!actual.getCorreo().equals(datos.getCorreo())
                && usuarioRepository.existsByCorreo(datos.getCorreo())) {

            throw new IllegalArgumentException(
                    "El correo ya está registrado");
        }

        actual.setRol(datos.getRol());
        actual.setNombre(datos.getNombre());
        actual.setApellido(datos.getApellido());
        actual.setCorreo(datos.getCorreo());
        actual.setEstado(datos.getEstado());

        if (datos.getContrasenaHash() != null
                && !datos.getContrasenaHash().isBlank()) {

            actual.setContrasenaHash(
                    datos.getContrasenaHash());
        }

        return usuarioRepository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Usuario actual = buscarPorId(id);
        usuarioRepository.delete(actual);
    }
}
