package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IRecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    boolean existsByReporte_IdReporte(Long idReporte);
    List<Recomendacion> findByReporte_IdReporte(Long idReporte);

    List<Recomendacion> findByPrioridadSugeridaIgnoreCase(String prioridadSugerida);

    @Query("""
        SELECT r.prioridad, COUNT(rec)
        FROM Recomendacion rec
        JOIN rec.reporte r
        GROUP BY r.prioridad
        """)
    List<Object[]> contarPorPrioridadDelReporte();
}
