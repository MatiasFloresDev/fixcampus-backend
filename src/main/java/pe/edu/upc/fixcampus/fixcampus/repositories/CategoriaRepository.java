package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Consulta 1: busca categorías por parte del nombre.
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    // Consulta 15: busca categorías cuya descripción contenga la palabra indicada.
    @Query("SELECT c FROM Categoria c WHERE lower(c.descripcion) LIKE lower(concat('%', :palabraClave, '%'))")
    List<Categoria> buscarPorDescripcion(@Param("palabraClave") String palabraClave);

    // Consulta 20: LEFT JOIN y COUNT; incluye categorías que tienen cero reportes.
    @Query(value = """
            SELECT c.id_categoria, c.nombre, c.descripcion, COUNT(r.id_reporte)
            FROM categoria c LEFT JOIN reporte r ON r.id_categoria = c.id_categoria
            GROUP BY c.id_categoria, c.nombre, c.descripcion
            ORDER BY COUNT(r.id_reporte) DESC, c.nombre
            """, nativeQuery = true)
    List<Object[]> contarReportesPorCategoria();
}
