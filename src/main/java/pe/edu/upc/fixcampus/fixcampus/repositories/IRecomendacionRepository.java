package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IRecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    boolean existsByReporte_IdReporte(Long idReporte);
    // Consulta 10: busca la única recomendación de mantenimiento del reporte indicado.
    Optional<Recomendacion> findByReporte_IdReporte(Long idReporte);
    
    // Consulta 8: busca recomendaciones de mantenimiento por prioridad sugerida.
    List<Recomendacion> findByPrioridadSugeridaIgnoreCase(String prioridadSugerida);

    // Consulta 9 con JOIN: relaciona recomendación, incidencia y categoría para filtrar por su nombre.
    @Query("select rec from Recomendacion rec join rec.reporte r join r.categoria c " +
            "where lower(c.nombre) = lower(:nombreCategoria)")
    List<Recomendacion> findByCategoriaDelReporte(@Param("nombreCategoria") String nombreCategoria);
}
