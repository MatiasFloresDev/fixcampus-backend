package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;

import java.util.List;

@Repository
public interface IReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByEstadoIgnoreCase(String estado);

    @Query("select r from Reporte r join r.categoria c where lower(c.nombre) = lower(:nombreCategoria)")
    List<Reporte> findByNombreCategoria(@Param("nombreCategoria") String nombreCategoria);

    @Query("select r from Reporte r join r.usuarioReportante u where lower(u.correo) = lower(:correo)")
    List<Reporte> findByCorreoReportante(@Param("correo") String correo);

    @Query(value = """
        SELECT u.id_usuario, u.nombre, u.apellido,
               EXTRACT(YEAR FROM r.fecha_creacion),
               EXTRACT(MONTH FROM r.fecha_creacion),
               COUNT(*)
        FROM reporte r
        JOIN usuario u ON r.id_usuario_reportante = u.id_usuario
        GROUP BY u.id_usuario,
                 EXTRACT(YEAR FROM r.fecha_creacion),
                 EXTRACT(MONTH FROM r.fecha_creacion)
        """, nativeQuery = true)
    List<Object[]> contarPorUsuarioYMes();


    List<Reporte> findByPrioridadIgnoreCase(String prioridad);

    @Query("""
        SELECT c.nombre, COUNT(r)
        FROM Reporte r
        JOIN r.categoria c
        GROUP BY c.nombre
        """)
    List<Object[]> contarPorCategoria();
    @Query(value = "SELECT EXISTS(SELECT 1 FROM comentario WHERE id_reporte=:id) OR EXISTS(SELECT 1 FROM adjunto WHERE id_reporte=:id) OR EXISTS(SELECT 1 FROM recomendacion WHERE id_reporte=:id)", nativeQuery = true)
    boolean tieneRelaciones(@Param("id") Long id);
}
