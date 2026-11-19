package pe.edu.upc.fixcampus.fixcampus.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);

    List<Usuario> findByNombre(String nombre);

    @Query(
        value = "SELECT r.nombre, COUNT(u.id_usuario) " +
            "FROM usuario u INNER JOIN rol r ON u.id_rol = r.id_rol " +
            "GROUP BY r.nombre ORDER BY COUNT(u.id_usuario) DESC",
        nativeQuery = true
    )
    List<Object[]> contarUsuariosPorRol();
}
