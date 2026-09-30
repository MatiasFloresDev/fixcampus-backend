package pe.edu.upc.fixcampus.fixcampus.securities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private static final long TIEMPO_TOKEN = 5 * 60 * 60;

    public JwtTokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generarToken(UserDetails usuario) {
        Instant ahora = Instant.now();
        List<String> nombresRoles = new ArrayList<>();

        for (GrantedAuthority autoridad : usuario.getAuthorities()) {
            nombresRoles.add(autoridad.getAuthority());
        }

        String roles = String.join(",", nombresRoles);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(usuario.getUsername())
                .issuedAt(ahora)
                .expiresAt(ahora.plusSeconds(TIEMPO_TOKEN))
                .claim("roles", roles)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }
}
