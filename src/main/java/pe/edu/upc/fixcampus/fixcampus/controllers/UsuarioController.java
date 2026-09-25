package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.RolRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {
    private final UsuarioRepository repository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository repository, RolRepository rolRepository,
                             PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Muestra todos los usuarios registrados. Solo para administradores.")
    public List<UsuarioDTO> listar() {
        return repository.findAll().stream().map(this::convertir).toList();
    }

    @GetMapping("/count")
    public long contarRegistrados() {
        return repository.count();
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscar(@PathVariable Long id) {
        return convertir(buscarEntidad(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioDTOInsert datos) {
        if (datos.getPassword() == null || datos.getPassword().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        Usuario usuario = new Usuario();
        copiarDatos(usuario, datos);
        usuario.setFechaRegistro(LocalDateTime.now());
        return ResponseEntity.status(201).body(convertir(repository.save(usuario)));
    }

    @PutMapping("/{id}")
    public UsuarioDTO actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTOInsert datos) {
        Usuario usuario = buscarEntidad(id);
        copiarDatos(usuario, datos);
        return convertir(repository.save(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repository.delete(buscarEntidad(id));
        return ResponseEntity.noContent().build();
    }

    private Usuario buscarEntidad(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private void copiarDatos(Usuario usuario, UsuarioDTOInsert datos) {
        Rol rol = rolRepository.findById(datos.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
        repository.findByCorreo(datos.getCorreo()).ifPresent(existente -> {
            if (!existente.getIdUsuario().equals(usuario.getIdUsuario())) {
                throw new IllegalArgumentException("El correo ya está registrado");
            }
        });
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

    private UsuarioDTO convertir(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setRolId(usuario.getRol().getIdRol());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        dto.setEstado(usuario.getEstado());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        return dto;
    }
}
