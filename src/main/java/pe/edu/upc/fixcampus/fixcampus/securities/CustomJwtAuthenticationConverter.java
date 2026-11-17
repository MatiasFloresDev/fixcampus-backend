package pe.edu.upc.fixcampus.fixcampus.securities;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String idUsuario = jwt.getSubject();
        if (idUsuario == null || !idUsuario.matches("[1-9][0-9]{0,18}")) {
            throw new BadCredentialsException("El identificador del usuario no es válido");
        }
        try {
            Long.parseLong(idUsuario);
        } catch (NumberFormatException error) {
            throw new BadCredentialsException("El identificador del usuario no es válido");
        }
        String roles = jwt.getClaimAsString("roles");
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (roles != null && !roles.isBlank()) {
            String[] rolesArray = roles.split(",");
            for (String role : rolesArray) {
                String nombre = role.trim();
                if (nombre.isBlank()) {
                    continue;
                }
                if (!nombre.startsWith("ROLE_")) {
                    nombre = "ROLE_" + nombre;
                }
                authorities.add(new SimpleGrantedAuthority(nombre));
            }
        }

        if (authorities.isEmpty()) {
            return new JwtAuthenticationToken(jwt, Collections.emptyList(), jwt.getSubject());
        }
        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }
}
