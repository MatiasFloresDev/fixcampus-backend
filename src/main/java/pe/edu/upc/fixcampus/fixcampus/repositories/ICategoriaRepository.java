package pe.edu.upc.fixcampus.fixcampus.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query("SELECT c FROM Categoria c WHERE lower(c.descripcion) LIKE lower(concat('%', :palabraClave, '%'))")
    List<Categoria> buscarPorDescripcion(@Param("palabraClave") String palabraClave);

    @Query(
        value = "SELECT c.id_categoria, c.nombre, c.descripcion, COUNT(r.id_reporte) " +
            "FROM categoria c LEFT JOIN reporte r ON r.id_categoria = c.id_categoria " +
            "GROUP BY c.id_categoria, c.nombre, c.descripcion " +
            "ORDER BY COUNT(r.id_reporte) DESC, c.nombre",
        nativeQuery = true
    )
    List<Object[]> contarReportesPorCategoria();
}
