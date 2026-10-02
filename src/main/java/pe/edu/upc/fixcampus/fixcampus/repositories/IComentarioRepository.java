package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;
import java.util.List;

@Repository
public interface IComentarioRepository extends JpaRepository<Comentario, Long> {
    // Consulta 9 (simple): busca comentarios cuyo texto contenga una palabra.
    List<Comentario> findByTextoComentarioContainingIgnoreCase(String texto);

    // Consulta 10 (un LEFT JOIN + COUNT): cuenta comentarios por incidencia e incluye las que tienen cero.
    @Query(value = "SELECT r.id_reporte, r.titulo, COUNT(c.id_comentario) " +
            "FROM reporte r LEFT JOIN comentario c ON c.id_reporte = r.id_reporte " +
            "GROUP BY r.id_reporte, r.titulo ORDER BY COUNT(c.id_comentario) DESC, r.id_reporte", nativeQuery = true)
    List<Object[]> contarPorReporte();
}
