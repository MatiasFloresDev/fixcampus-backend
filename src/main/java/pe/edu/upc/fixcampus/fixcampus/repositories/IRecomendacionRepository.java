package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;
import java.util.List;

public interface IRecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    // Auxiliar del CRUD: evita guardar dos recomendaciones para la misma incidencia.
    boolean existsByReporte_IdReporte(Long idReporte);

    // Consulta 7 (simple): muestra recomendaciones de la prioridad sugerida indicada.
    List<Recomendacion> findByPrioridadSugeridaIgnoreCase(String prioridadSugerida);

    // Consulta 8 (un JOIN + COUNT): cuenta recomendaciones según la prioridad real de la incidencia.
    @Query(value = "SELECT r.prioridad, COUNT(rec.id_recomendacion) " +
            "FROM recomendacion rec INNER JOIN reporte r ON rec.id_reporte = r.id_reporte " +
            "GROUP BY r.prioridad ORDER BY COUNT(rec.id_recomendacion) DESC, r.prioridad", nativeQuery = true)
    List<Object[]> contarPorPrioridadReporte();
}
