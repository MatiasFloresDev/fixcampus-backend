package pe.edu.upc.fixcampus.fixcampus.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;

public interface IRecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    boolean existsByReporte_IdReporte(Long idReporte);

    Optional<Recomendacion> findByReporte_IdReporte(Long reporteId);

    List<Recomendacion> findByPrioridadSugeridaIgnoreCase(String prioridadSugerida);

    @Query(
        value = "SELECT r.prioridad, COUNT(rec.id_recomendacion) " +
            "FROM recomendacion rec INNER JOIN reporte r ON rec.id_reporte = r.id_reporte " +
            "GROUP BY r.prioridad ORDER BY COUNT(rec.id_recomendacion) DESC, r.prioridad",
        nativeQuery = true
    )
    List<Object[]> contarPorPrioridadReporte();
}
