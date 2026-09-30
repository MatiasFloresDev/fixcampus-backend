package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    boolean existsByReporte_IdReporte(Long idReporte);
    
    Optional<Recomendacion> findByReporte_IdReporte(Long idReporte);
    
// Consulta 11: filtra recomendaciones por la prioridad sugerida (ALTA, MEDIA, BAJA).
    List<Recomendacion> findByPrioridadSugeridaIgnoreCase(String prioridadSugerida);

    // Consulta 12, con JOIN: recomendaciones de los reportes que pertenecen a una categoría.
    @Query("select rec from Recomendacion rec join rec.reporte r join r.categoria c " +
            "where lower(c.nombre) = lower(:nombreCategoria)")
    List<Recomendacion> findByCategoriaDelReporte(@Param("nombreCategoria") String nombreCategoria);
}
}
