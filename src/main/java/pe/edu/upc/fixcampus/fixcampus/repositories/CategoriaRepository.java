package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.dtos.CategoriaConReportesDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Query 1 — simple: busca categorías cuya descripción contenga una palabra clave
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT c FROM Categoria c WHERE lower(c.descripcion) LIKE lower(concat('%', :palabraClave, '%'))")
    List<Categoria> buscarPorDescripcion(@Param("palabraClave") String palabraClave);

    // Query 2 — con JOIN y COUNT: une Categoria con Reporte y cuenta cuántos reportes tiene cada categoría
    @Query("SELECT new pe.edu.upc.fixcampus.fixcampus.dtos.CategoriaConReportesDTO(" +
           "c.idCategoria, c.nombre, c.descripcion, COUNT(r)) " +
           "FROM Categoria c LEFT JOIN Reporte r ON r.categoria = c " +
           "GROUP BY c.idCategoria, c.nombre, c.descripcion " +
           "ORDER BY COUNT(r) DESC")
    List<CategoriaConReportesDTO> contarReportesPorCategoria();
}