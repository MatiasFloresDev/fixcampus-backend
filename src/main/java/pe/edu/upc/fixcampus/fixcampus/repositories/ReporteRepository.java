package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;

import java.util.List;

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

    // Consulta 10, con JOIN: cuenta las incidencias creadas por cada usuario en cada mes.
    @Query("select new pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO(" +
            "u.idUsuario, u.nombre, u.apellido, year(r.fechaCreacion), " +
            "month(r.fechaCreacion), count(r)) " +
            "from Reporte r join r.usuarioReportante u " +
            "group by u.idUsuario, u.nombre, u.apellido, " +
            "year(r.fechaCreacion), month(r.fechaCreacion) " +
            "order by year(r.fechaCreacion), month(r.fechaCreacion), u.nombre")
    List<IncidenciasPorMesDTO> contarPorUsuarioYMes();

    // Consulta 11, con JOIN: cuenta las incidencias de cada campus según su estado.
    @Query("select new pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO(" +
            "u.campus, count(r)) from Reporte r join r.ubicacion u " +
            "where lower(r.estado) = lower(:estado) " +
            "group by u.campus order by count(r) desc")
    List<IncidenciasPorCampusDTO> contarPorCampusYEstado(@Param("estado") String estado);
    
        // Consulta 12: Obtiene el listado completo de reportes filtrados por su nivel de prioridad.
    @Query("SELECT r FROM Reporte r WHERE r.prioridad = :prioridad")
    List<Reporte> findByPrioridad(@Param("prioridad") String prioridad);

    // Consulta 13, con JOIN: Obtiene el listado de reportes registrados en un campus específico.
    @Query("SELECT r FROM Reporte r INNER JOIN r.ubicacion u " +
            "WHERE LOWER(u.campus) = LOWER(:campus)")
    List<Reporte> findByCampus(
            @Param("campus") String campus);
}

