package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;

import java.util.List;

@Repository
public interface IReporteRepository extends JpaRepository<Reporte, Long> {

    // Consulta 1: filtra incidencias por estado (ABIERTO, RESUELTO, etc.).
    List<Reporte> findByEstadoIgnoreCase(String estado);

    // Método auxiliar: obtiene los reportes propios usando el correo del token.
    @Query("select r from Reporte r join r.usuarioReportante u where lower(u.correo) = lower(:correo)")
    List<Reporte> findByCorreoReportante(@Param("correo") String correo);

    // Consulta 2: JOIN con usuario; COUNT cuenta incidencias y GROUP BY separa usuario/año/mes.
    @Query(value = """
            SELECT u.id_usuario, u.nombre, u.apellido,
                   EXTRACT(YEAR FROM r.fecha_creacion), EXTRACT(MONTH FROM r.fecha_creacion), COUNT(*)
            FROM reporte r INNER JOIN usuario u ON r.id_usuario_reportante = u.id_usuario
            GROUP BY u.id_usuario, u.nombre, u.apellido,
                     EXTRACT(YEAR FROM r.fecha_creacion), EXTRACT(MONTH FROM r.fecha_creacion)
            ORDER BY EXTRACT(YEAR FROM r.fecha_creacion), EXTRACT(MONTH FROM r.fecha_creacion), u.nombre
            """, nativeQuery = true)
    List<Object[]> contarPorUsuarioYMes();

    // Consulta 4: filtra las incidencias por su nivel de prioridad.
    @Query("select r from Reporte r where lower(r.prioridad) = lower(:prioridad)")
    List<Reporte> findByPrioridad(@Param("prioridad") String prioridad);

    // Consulta 5, con JOIN: busca las incidencias registradas en un campus.
    @Query("select r from Reporte r join r.ubicacion u " +
            "where lower(u.campus) = lower(:campus)")
    List<Reporte> findByCampus(@Param("campus") String campus);

}
