package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import java.util.List;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    // Consulta 5 (simple): busca categorías por una palabra de su descripción.
    @Query("SELECT c FROM Categoria c WHERE lower(c.descripcion) LIKE lower(concat('%', :palabraClave, '%'))")
    List<Categoria> buscarPorDescripcion(@Param("palabraClave") String palabraClave);

    // Consulta 6 (un LEFT JOIN + COUNT): cuenta reportes por categoría e incluye las que tienen cero.
    @Query(value = "SELECT c.id_categoria, c.nombre, c.descripcion, COUNT(r.id_reporte) " +
            "FROM categoria c LEFT JOIN reporte r ON r.id_categoria = c.id_categoria " +
            "GROUP BY c.id_categoria, c.nombre, c.descripcion " +
            "ORDER BY COUNT(r.id_reporte) DESC, c.nombre", nativeQuery = true)
    List<Object[]> contarReportesPorCategoria();
}
