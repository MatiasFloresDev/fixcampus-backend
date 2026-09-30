package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    // Consulta 7: filtra incidencias por estado (ABIERTO, RESUELTO, etc.).
    List<Reporte> findByEstadoIgnoreCase(String estado);

    // Consulta 8, con JOIN: busca incidencias por el nombre de su categoría.
    @Query("select r from Reporte r join r.categoria c where lower(c.nombre) = lower(:nombreCategoria)")
    List<Reporte> findByNombreCategoria(@Param("nombreCategoria") String nombreCategoria);

    // Consulta 9, con JOIN: muestra incidencias hechas por un usuario según su correo.
    @Query("select r from Reporte r join r.usuarioReportante u where lower(u.correo) = lower(:correo)")
    List<Reporte> findByCorreoReportante(@Param("correo") String correo);

    // Consulta 10: JOIN con usuario; COUNT cuenta incidencias y GROUP BY separa usuario/año/mes.
    @Query(value = """
            SELECT u.id_usuario, u.nombre, u.apellido,
                   EXTRACT(YEAR FROM r.fecha_creacion), EXTRACT(MONTH FROM r.fecha_creacion), COUNT(*)
            FROM reporte r INNER JOIN usuario u ON r.id_usuario_reportante = u.id_usuario
            GROUP BY u.id_usuario, u.nombre, u.apellido,
                     EXTRACT(YEAR FROM r.fecha_creacion), EXTRACT(MONTH FROM r.fecha_creacion)
            ORDER BY EXTRACT(YEAR FROM r.fecha_creacion), EXTRACT(MONTH FROM r.fecha_creacion), u.nombre
            """, nativeQuery = true)
    List<Object[]> contarPorUsuarioYMes();

    // Consulta 11: JOIN con ubicación; WHERE filtra estado y COUNT agrupa incidencias por campus.
    @Query(value = """
            SELECT u.campus, COUNT(*)
            FROM reporte r INNER JOIN ubicacion u ON r.id_ubicacion = u.id_ubicacion
            WHERE LOWER(r.estado) = LOWER(:estado)
            GROUP BY u.campus ORDER BY COUNT(*) DESC
            """, nativeQuery = true)
    List<Object[]> contarPorCampusYEstado(@Param("estado") String estado);

    // Consulta 12: filtra las incidencias por su nivel de prioridad.
    @Query("select r from Reporte r where lower(r.prioridad) = lower(:prioridad)")
    List<Reporte> findByPrioridad(@Param("prioridad") String prioridad);

    // Consulta 13, con JOIN: busca las incidencias registradas en un campus.
    @Query("select r from Reporte r join r.ubicacion u " +
            "where lower(u.campus) = lower(:campus)")
    List<Reporte> findByCampus(@Param("campus") String campus);

    // Consulta 18: JOIN con categoría; cuenta incidencias dentro del periodo [desde, hasta).
    @Query(value = """
            SELECT c.id_categoria, c.nombre, COUNT(*)
            FROM reporte r INNER JOIN categoria c ON r.id_categoria = c.id_categoria
            WHERE r.fecha_creacion >= :desde AND r.fecha_creacion < :hasta
            GROUP BY c.id_categoria, c.nombre ORDER BY COUNT(*) DESC, c.nombre
            """, nativeQuery = true)
    List<Object[]> contarPorCategoriaEntreFechas(@Param("desde") LocalDateTime desde,
                                                @Param("hasta") LocalDateTime hasta);
}
