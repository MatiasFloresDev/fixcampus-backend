package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.dtos.RegistroRequestDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTOInsert;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
    List<Usuario> buscarPorEstado(String estado);
    long contarRegistrados();
    Usuario buscarPorId(Long id);
    Usuario crear(UsuarioDTOInsert datos);
    Usuario actualizar(Long id, UsuarioDTOInsert datos);
    Usuario registrar(RegistroRequestDTO datos);
    void eliminar(Long id);
}
