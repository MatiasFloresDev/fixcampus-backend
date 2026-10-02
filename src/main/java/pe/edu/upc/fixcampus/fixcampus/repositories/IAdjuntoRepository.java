package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;

import java.util.List;

@Repository
public interface IAdjuntoRepository extends JpaRepository<Adjunto, Long> {
    // Método auxiliar: lista las evidencias del reporte después de comprobar su propietario.
    List<Adjunto> findByReporte_IdReporte(Long reporteId);

}
