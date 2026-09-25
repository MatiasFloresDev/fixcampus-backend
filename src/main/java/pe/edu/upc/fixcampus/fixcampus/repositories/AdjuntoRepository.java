package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;

import java.util.List;

@Repository
public interface AdjuntoRepository extends JpaRepository<Adjunto, Long> {
    // Consulta 5: busca archivos adjuntos por tipo (imagen, PDF, etc.).
    List<Adjunto> findByTipoArchivoContainingIgnoreCase(String tipoArchivo);

    List<Adjunto> findByReporte_IdReporte(Long reporteId);

    long countByReporte_IdReporte(Long reporteId);

    java.util.Optional<Adjunto> findByUrlArchivo(String urlArchivo);
}
