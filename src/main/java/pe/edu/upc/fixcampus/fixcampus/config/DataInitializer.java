package pe.edu.upc.fixcampus.fixcampus.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.repositories.RolRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.CategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UbicacionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner crearUsuariosDePrueba(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            CategoriaRepository categoriaRepository,
            UbicacionRepository ubicacionRepository) {
        return args -> {
            Rol administrador = crearRol(rolRepository, "ADMIN", "TOTAL");
            Rol usuario = crearRol(rolRepository, "USUARIO", "BASICO");

            crearUsuario(usuarioRepository, passwordEncoder,
                    "admin@fixcampus.com", "admin123", "Administrador",
                    "FixCampus", administrador);
            crearUsuario(usuarioRepository, passwordEncoder,
                    "usuario@fixcampus.com", "usuario123", "Usuario",
                    "FixCampus", usuario);

            crearCategorias(categoriaRepository);
            crearUbicaciones(ubicacionRepository);
        };
    }

    private void crearCategorias(CategoriaRepository repository) {
        crearCategoria(repository, "Infraestructura", "Paredes, techos, pisos y espacios del campus.");
        crearCategoria(repository, "Electricidad", "Luces, tomacorrientes y cortes de energía.");
        crearCategoria(repository, "Limpieza", "Residuos, derrames y mantenimiento de ambientes.");
        crearCategoria(repository, "Equipamiento", "Proyectores, computadoras y equipos de aula.");
        crearCategoria(repository, "Seguridad", "Señalización, accesos y situaciones de riesgo.");
        crearCategoria(repository, "Internet y red", "Conectividad Wi-Fi, red cableada y puntos de acceso.");
        crearCategoria(repository, "Mobiliario", "Carpetas, sillas, mesas y estantes.");
        crearCategoria(repository, "Climatización", "Ventilación, aire acondicionado y temperatura.");
        crearCategoria(repository, "Baños", "Grifería, sanitarios y suministros de los servicios higiénicos.");
        crearCategoria(repository, "Accesibilidad", "Rampas, ascensores y señalización accesible.");
    }

    private void crearCategoria(CategoriaRepository repository, String nombre, String descripcion) {
        if (repository.findByNombreIgnoreCase(nombre).isPresent()) {
            return;
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        repository.save(categoria);
    }

    private void crearUbicaciones(UbicacionRepository repository) {
        List<Ubicacion> existentes = repository.findAll();
        crearUbicacion(repository, existentes, "Monterrico", "A", 1, "Pasillo", "Aula");
        crearUbicacion(repository, existentes, "Monterrico", "B", 1, "Pasillo", "Aula");
        crearUbicacion(repository, existentes, "Monterrico", "C", 2, "Laboratorio", "Laboratorio");
        crearUbicacion(repository, existentes, "San Miguel", "Pabellón A", 1, "Pasadizo principal", "Aula");
        crearUbicacion(repository, existentes, "San Miguel", "Pabellón B", 2, "Zona de escaleras", "Aula");
        crearUbicacion(repository, existentes, "Villa", "Pabellón central", 1, "Patio", "Campus");
        crearUbicacion(repository, existentes, "Villa", "Biblioteca", 1, "Sala de lectura", "Biblioteca");
    }

    private void crearUbicacion(UbicacionRepository repository,
                                List<Ubicacion> existentes,
                                String campus,
                                String edificio,
                                Integer piso,
                                String zona,
                                String tipo) {
        boolean existe = existentes.stream().anyMatch(item ->
                mismoTexto(item.getCampus(), campus)
                        && mismoTexto(item.getEdificio(), edificio)
                        && java.util.Objects.equals(item.getPiso(), piso)
                        && mismoTexto(item.getZona(), zona));
        if (existe) {
            return;
        }

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setCampus(campus);
        ubicacion.setEdificio(edificio);
        ubicacion.setPiso(piso);
        ubicacion.setZona(zona);
        ubicacion.setTipo(tipo);
        existentes.add(repository.save(ubicacion));
    }

    private boolean mismoTexto(String actual, String esperado) {
        return actual != null && esperado != null && actual.equalsIgnoreCase(esperado);
    }

    private Rol crearRol(RolRepository repository, String nombre, String nivel) {
        return repository.findByNombre(nombre).orElseGet(() -> {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rol.setNivelAcceso(nivel);
            rol.setDescripcion("Rol de prueba para FixCampus");
            return repository.save(rol);
        });
    }

    private void crearUsuario(UsuarioRepository repository,
                              PasswordEncoder passwordEncoder,
                              String correo,
                              String password,
                              String nombre,
                              String apellido,
                              Rol rol) {
        if (repository.findByCorreo(correo).isPresent()) {
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setRol(rol);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setContrasenaHash(passwordEncoder.encode(password));
        usuario.setEstado("ACTIVO");
        usuario.setFechaRegistro(LocalDateTime.now());
        repository.save(usuario);
    }
}
