package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    boolean existsByReporte_IdReporte(Long idReporte);
    
    List<Recomendacion> findByPrioridadSugeridaIgnoreCase(String prioridadSugerida);


    @Query("select rec from Recomendacion rec join rec.reporte r join r.categoria c " +
            "where lower(c.nombre) = lower(:nombreCategoria)")
    List<Recomendacion> findByCategoriaDelReporte(@Param("nombreCategoria") String nombreCategoria);
}
