package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;

import java.util.List;

@Repository
public interface AdjuntoRepository extends JpaRepository<Adjunto, Long> {
    // Consulta 19: JOIN con usuario y LEFT JOIN con adjunto; cuenta las evidencias del correo indicado.
    // LEFT JOIN incluye sus incidencias sin adjuntos; COUNT(id_adjunto) no cuenta los valores nulos.
    @Query(value = """
            SELECT u.id_usuario, u.nombre, u.apellido, COUNT(a.id_adjunto)
            FROM reporte r INNER JOIN usuario u ON r.id_usuario_reportante = u.id_usuario
            LEFT JOIN adjunto a ON a.id_reporte = r.id_reporte
            WHERE LOWER(u.correo) = LOWER(:correo)
            GROUP BY u.id_usuario, u.nombre, u.apellido
            ORDER BY u.nombre
            """, nativeQuery = true)
    List<Object[]> contarEvidenciasPorUsuario(@Param("correo") String correo);
    // Consulta 5: busca archivos adjuntos por tipo (imagen, PDF, etc.).
    List<Adjunto> findByTipoArchivoContainingIgnoreCase(String tipoArchivo);

    List<Adjunto> findByReporte_IdReporte(Long reporteId);

    long countByReporte_IdReporte(Long reporteId);

    java.util.Optional<Adjunto> findByUrlArchivo(String urlArchivo);
}
