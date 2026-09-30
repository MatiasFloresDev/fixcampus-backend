package pe.edu.upc.fixcampus.fixcampus.servicesimpl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public JwtUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo)
            throws UsernameNotFoundException {
        Optional<Usuario> encontrado = usuarioRepository.findByCorreo(correo);
        if (encontrado.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + correo);
        }
        Usuario usuario = encontrado.get();

        String nombreRol = usuario.getRol().getNombre();
        String autoridad;
        if (nombreRol.startsWith("ROLE_")) {
            autoridad = nombreRol;
        } else {
            autoridad = "ROLE_" + nombreRol;
        }

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(autoridad));

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getContrasenaHash())
                .authorities(authorities)
                .disabled(!"ACTIVO".equalsIgnoreCase(usuario.getEstado()))
                .build();
    }
}
