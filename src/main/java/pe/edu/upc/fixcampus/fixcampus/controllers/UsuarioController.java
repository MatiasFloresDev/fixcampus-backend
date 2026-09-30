package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import java.util.ArrayList;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.RegistroRequestDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.RegistroResponseDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;

import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.UsuarioService;
import java.util.List;

@RestController
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) { this.service = service; }

    @GetMapping("/api/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuarios", description = "Muestra todos los usuarios registrados sin pedir parámetros y sin devolver contraseñas. Solo para administradores.")
    public List<UsuarioDTO> listar() {
        return convertirLista(service.listar());
    }

    @GetMapping("/api/users/estado")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar usuarios por estado", description = "Consulta 4: filtra usuarios por estado, por ejemplo ACTIVO. Solo para administradores.")
    public List<UsuarioDTO> buscarPorEstado(@RequestParam String estado) {
        return convertirLista(service.buscarPorEstado(estado));
    }

    @GetMapping("/api/users/count")
    @PreAuthorize("hasRole('ADMIN')")
    public long contarRegistrados() {
        return service.contarRegistrados();
    }

    @GetMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioDTO buscar(@PathVariable Long id) {
        return convertir(service.buscarPorId(id));
    }

    @PostMapping("/api/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioDTOInsert datos) {
        Usuario guardado = service.crear(datos);
        UsuarioDTO respuesta = convertir(guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioDTO actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTOInsert datos) {
        return convertir(service.actualizar(id, datos));
    }

    @DeleteMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/registro")
    @SecurityRequirements
    @ApiResponse(responseCode = "201", description = "Cuenta creada correctamente")
    @Operation(summary = "Registrar una cuenta", description = "Ruta pública. Crea un usuario ACTIVO con rol USUARIO. La contraseña debe tener al menos 6 caracteres y se guarda como hash BCrypt.")
    public ResponseEntity<RegistroResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO request) {
        Usuario guardado = service.registrar(request);
        RegistroResponseDTO response = new RegistroResponseDTO(
                guardado.getIdUsuario(), guardado.getCorreo(), "Cuenta creada correctamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    private List<UsuarioDTO> convertirLista(List<Usuario> usuarios) {
        List<UsuarioDTO> lista = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            lista.add(convertir(usuario));
        }
        return lista;
    }
}
