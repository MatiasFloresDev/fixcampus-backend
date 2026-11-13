package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import java.util.ArrayList;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.RegistroRequestDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.RegistroResponseDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTOList;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuariosPorRolDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;

import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUsuarioService;
import java.util.List;

@RestController
public class UsuarioController {
    private final ModelMapper modelMapper;
    private final IUsuarioService service;

    public UsuarioController(IUsuarioService service, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.service = service;
    }

    @GetMapping("/api/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuarios", description = "Muestra todos los usuarios registrados sin pedir parámetros y sin devolver contraseñas. Solo para administradores.")
    public ResponseEntity<List<UsuarioDTOList>> listar() {
        return ResponseEntity.ok(convertirLista(service.listar()));
    }

    @GetMapping("/api/users/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> contarRegistrados() {
        return ResponseEntity.ok(service.contarRegistrados());
    }

    @GetMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTOList> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(convertir(service.buscarPorId(id)));
    }

    @PostMapping("/api/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTOList> crear(@Valid @RequestBody UsuarioDTOInsert datos) {
        Usuario guardado = service.crear(datos);
        UsuarioDTOList respuesta = convertir(guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTOList> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTOInsert datos) {
        return ResponseEntity.ok(convertir(service.actualizar(id, datos)));
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

    @GetMapping("/api/users/nombre")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar usuarios por nombre", description = "Consulta 9, simple: lista usuarios cuyo nombre coincide exactamente con el parámetro nombre. Ejemplo: nombre=Matias. No devuelve contraseñas. Solo administradores.")
    public ResponseEntity<List<UsuarioDTOList>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(convertirLista(service.buscarPorNombre(nombre)));
    }

    @GetMapping("/api/users/por-rol")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Contar usuarios por rol", description = "Consulta 10: un INNER JOIN y COUNT. Cuenta los usuarios registrados de cada rol y ordena de mayor a menor cantidad. Incluye solo roles con usuarios. No pide parámetros. Solo administradores.")
    public ResponseEntity<List<UsuariosPorRolDTO>> contarUsuariosPorRol() {
        List<UsuariosPorRolDTO> lista = new ArrayList<>();
        for (Object[] fila : service.contarUsuariosPorRol()) {
            UsuariosPorRolDTO dto = new UsuariosPorRolDTO(
                    (String) fila[0], ((Number) fila[1]).longValue());
            lista.add(dto);
        }
        return ResponseEntity.ok(lista);
    }

    private UsuarioDTOList convertir(Usuario usuario) {
        UsuarioDTOList dto = modelMapper.map(usuario, UsuarioDTOList.class);
        dto.setRolId(usuario.getRol().getIdRol());
        return dto;
    }

    private List<UsuarioDTOList> convertirLista(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(item -> convertir(item))
                .toList();
    }
}
