package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    // Consulta 6: lista los comentarios de un reporte concreto mediante su relación.
    List<Comentario> findByReporte_IdReporte(Long idReporte);

    // Consulta 14: JOIN con reporte y usuario; filtra correo y cuenta comentarios de cada incidencia.
    @Query(value = """
            SELECT r.id_reporte, r.titulo, COUNT(*)
            FROM comentario c INNER JOIN reporte r ON c.id_reporte = r.id_reporte
            INNER JOIN usuario u ON c.id_usuario = u.id_usuario
            WHERE LOWER(u.correo) = LOWER(:correo)
            GROUP BY r.id_reporte, r.titulo ORDER BY COUNT(*) DESC
            """, nativeQuery = true)
    List<Object[]> contarPorReporteYCorreo(@Param("correo") String correo);
}
