package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.fixcampus.fixcampus.dtos.LoginRequestDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.LoginResponseDTO;
import pe.edu.upc.fixcampus.fixcampus.securities.JwtTokenService;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUsuarioService;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;

import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final IUsuarioService usuarioService;

    public LoginController(AuthenticationManager authenticationManager,
                           JwtTokenService jwtTokenService,
                           IUsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @SecurityRequirements
    @Operation(summary = "Iniciar sesión", description = "Ruta pública. Valida correo y contraseña con BCrypt. Devuelve un token JWT firmado con HS512 y los roles; credenciales incorrectas producen 401.")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(), request.getPassword()));

        UserDetails usuario = (UserDetails) authentication.getPrincipal();
        Optional<Usuario> usuarioEncontrado = usuarioService.buscarPorCorreo(usuario.getUsername());
        if (usuarioEncontrado.isEmpty()) {
            throw new BadCredentialsException("Usuario no encontrado");
        }
        Long idUsuario = usuarioEncontrado.get().getIdUsuario();
        String token = jwtTokenService.generarToken(usuario, idUsuario);
        return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getUsername(), idUsuario));
    }
}
