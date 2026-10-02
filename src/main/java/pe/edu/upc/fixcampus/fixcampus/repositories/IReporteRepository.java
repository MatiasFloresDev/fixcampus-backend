package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import java.util.List;

@Repository
public interface IReporteRepository extends JpaRepository<Reporte, Long> {
    // Consulta 1 (simple): muestra las incidencias del estado indicado.
    List<Reporte> findByEstadoIgnoreCase(String estado);

    // Consulta 2 (un JOIN + COUNT): cuenta incidencias por usuario, año y mes.
    @Query(value = "SELECT u.id_usuario, u.nombre, u.apellido, " +
            "EXTRACT(YEAR FROM r.fecha_creacion), EXTRACT(MONTH FROM r.fecha_creacion), COUNT(*) " +
            "FROM reporte r INNER JOIN usuario u ON r.id_usuario_reportante = u.id_usuario " +
            "GROUP BY u.id_usuario, u.nombre, u.apellido, " +
            "EXTRACT(YEAR FROM r.fecha_creacion), EXTRACT(MONTH FROM r.fecha_creacion) " +
            "ORDER BY EXTRACT(YEAR FROM r.fecha_creacion), EXTRACT(MONTH FROM r.fecha_creacion), u.nombre",
            nativeQuery = true)
    List<Object[]> contarPorUsuarioYMes();

    // Consulta 3 (simple): muestra las incidencias de una prioridad, por ejemplo ALTA.
    @Query("select r from Reporte r where lower(r.prioridad) = lower(:prioridad)")
    List<Reporte> findByPrioridad(@Param("prioridad") String prioridad);

    // Consulta 4 (un JOIN + COUNT): cuenta incidencias por campus y estado.
    @Query(value = "SELECT u.campus, COUNT(r.id_reporte) " +
            "FROM reporte r INNER JOIN ubicacion u ON r.id_ubicacion = u.id_ubicacion " +
            "WHERE LOWER(r.estado) = LOWER(:estado) " +
            "GROUP BY u.campus ORDER BY COUNT(r.id_reporte) DESC, u.campus", nativeQuery = true)
    List<Object[]> contarPorCampusYEstado(@Param("estado") String estado);

    // Auxiliar de seguridad: obtiene los reportes propios usando el correo del token.
    @Query("select r from Reporte r join r.usuarioReportante u where lower(u.correo) = lower(:correo)")
    List<Reporte> findByCorreoReportante(@Param("correo") String correo);
}
