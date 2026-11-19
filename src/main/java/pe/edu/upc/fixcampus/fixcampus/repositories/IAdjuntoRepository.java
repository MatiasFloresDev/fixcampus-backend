package pe.edu.upc.fixcampus.fixcampus.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;

@Repository
public interface IAdjuntoRepository extends JpaRepository<Adjunto, Long> {
    List<Adjunto> findByReporte_IdReporte(Long reporteId);
}
