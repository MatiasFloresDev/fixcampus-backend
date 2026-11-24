package pe.edu.upc.fixcampus.fixcampus.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Notificacion;
@Repository
public interface INotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findAllByOrderByFechaDesc();
    List<Notificacion> findByReporte_IdReporte(Long reporteId);
}
