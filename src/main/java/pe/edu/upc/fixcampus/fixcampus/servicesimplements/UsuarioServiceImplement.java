package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUsuarioService;
import pe.edu.upc.fixcampus.fixcampus.repositories.*;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.dtos.RegistroRequestDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTOInsert;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImplement implements IUsuarioService {
    private final IUsuarioRepository repository;
    private final IRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImplement(IUsuarioRepository repository, IRolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listar() { return repository.findAll(); }
    public long contarRegistrados() { return repository.count(); }
    public void eliminar(Long id) { repository.delete(buscarPorId(id)); }

    public Usuario crear(UsuarioDTOInsert datos) {
        if (datos.getPassword() == null || datos.getPassword().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        Usuario usuario = new Usuario();
        copiarDatos(usuario, datos);
        usuario.setFechaRegistro(LocalDateTime.now());
        return repository.save(usuario);
    }

    public Usuario actualizar(Long id, UsuarioDTOInsert datos) {
        Usuario usuario = buscarPorId(id);
        copiarDatos(usuario, datos);
        return repository.save(usuario);
    }

    public Usuario registrar(RegistroRequestDTO request) {
        if (repository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Optional<Rol> rolEncontrado = rolRepository.findByNombre("USUARIO");
        if (rolEncontrado.isEmpty()) {
            throw new ResourceNotFoundException("Rol USUARIO no encontrado");
        }

        Usuario usuario = new Usuario();
        usuario.setRol(rolEncontrado.get());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasenaHash(passwordEncoder.encode(request.getPassword()));
        usuario.setEstado("ACTIVO");
        usuario.setFechaRegistro(LocalDateTime.now());

        return repository.save(usuario);
    }
    @Override
    public List<Usuario> buscarPorNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    public Usuario buscarPorId(Long id) {
        Optional<Usuario> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        return encontrado.get();
    }

    private void copiarDatos(Usuario usuario, UsuarioDTOInsert datos) {
        Optional<Rol> rolEncontrado = rolRepository.findById(datos.getRolId());
        if (rolEncontrado.isEmpty()) {
            throw new ResourceNotFoundException("Rol no encontrado");
        }
        Rol rol = rolEncontrado.get();
        Optional<Usuario> usuarioExistente = repository.findByCorreo(datos.getCorreo());
        if (usuarioExistente.isPresent()) {
            Usuario existente = usuarioExistente.get();
            if (!existente.getIdUsuario().equals(usuario.getIdUsuario())) {
                throw new IllegalArgumentException("El correo ya está registrado");
            }
        }
        usuario.setRol(rol);
        usuario.setNombre(datos.getNombre());
        usuario.setApellido(datos.getApellido());
        usuario.setCorreo(datos.getCorreo());
        usuario.setEstado(datos.getEstado());
        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            if (datos.getPassword().length() < 6) {
                throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
            }
            usuario.setContrasenaHash(passwordEncoder.encode(datos.getPassword()));
        }
    }

    @Override
    public List<Object[]> contarUsuariosPorRol() {
        return repository.contarUsuariosPorRol();
    }
}
