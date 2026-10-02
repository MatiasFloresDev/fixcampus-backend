package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    // Consulta 9 (simple): busca usuarios por nombre exacto, sin JOIN ni agregado.
    List<Usuario> findByNombre(String nombre);

    // Consulta 10 (un INNER JOIN + COUNT): cuenta usuarios por rol; excluye roles sin usuarios.
    @Query(value = "SELECT r.nombre, COUNT(u.id_usuario) " +
            "FROM usuario u INNER JOIN rol r ON u.id_rol = r.id_rol " +
            "GROUP BY r.nombre ORDER BY COUNT(u.id_usuario) DESC", nativeQuery = true)
    List<Object[]> contarUsuariosPorRol();
}
