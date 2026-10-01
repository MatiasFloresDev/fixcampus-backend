package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTOList;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTOUpdate;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IRolService;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUsuarioService;

import java.util.List;

@RestController
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final IRolService rolService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(
            IUsuarioService usuarioService,
            IRolService rolService,
            ModelMapper modelMapper,
            PasswordEncoder passwordEncoder) {

        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/api/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTOList>> listar() {

        List<UsuarioDTOList> lista =
                usuarioService.listar()
                        .stream()
                        .map(usuario ->
                                modelMapper.map(
                                        usuario,
                                        UsuarioDTOList.class
                                )
                        )
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/api/users/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTOList>> buscarPorEstado(
            @RequestParam String estado) {

        List<UsuarioDTOList> lista =
                usuarioService.buscarPorEstado(estado)
                        .stream()
                        .map(usuario ->
                                modelMapper.map(
                                        usuario,
                                        UsuarioDTOList.class
                                )
                        )
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/api/users/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> contarRegistrados() {

        long cantidad =
                usuarioService.contarRegistrados();

        return ResponseEntity.ok(cantidad);
    }

    @GetMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTOList> buscarPorId(
            @PathVariable Long id) {

        Usuario usuario =
                usuarioService.buscarPorId(id);

        UsuarioDTOList dto =
                modelMapper.map(
                        usuario,
                        UsuarioDTOList.class
                );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTOUpdate dto) {

        Rol rol =
                rolService.buscarPorId(
                        dto.getRolId()
                );

        Usuario usuario =
                modelMapper.map(
                        dto,
                        Usuario.class
                );

        usuario.setRol(rol);

        if (dto.getContrasena() != null
                && !dto.getContrasena().isBlank()) {

            usuario.setContrasenaHash(
                    passwordEncoder.encode(
                            dto.getContrasena()
                    )
            );
        }

        Usuario actualizado =
                usuarioService.actualizar(
                        id,
                        usuario
                );

        UsuarioDTOList responseDTO =
                modelMapper.map(
                        actualizado,
                        UsuarioDTOList.class
                );

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        usuarioService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTOList> registrar(
            @Valid @RequestBody UsuarioDTOInsert dto) {

        Rol rolUsuario =
                rolService.buscarPorNombreExacto(
                        "ROLE_USUARIO"
                );

        Usuario usuario =
                modelMapper.map(
                        dto,
                        Usuario.class
                );

        usuario.setRol(rolUsuario);

        usuario.setContrasenaHash(
                passwordEncoder.encode(
                        dto.getContrasena()
                )
        );

        Usuario guardado =
                usuarioService.registrar(usuario);

        UsuarioDTOList responseDTO =
                modelMapper.map(
                        guardado,
                        UsuarioDTOList.class
                );

        return ResponseEntity
                .status(201)
                .body(responseDTO);
    }
}