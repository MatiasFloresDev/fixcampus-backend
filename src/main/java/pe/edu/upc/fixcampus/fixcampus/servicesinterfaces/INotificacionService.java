package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;
import java.util.List;
import pe.edu.upc.fixcampus.fixcampus.entities.Notificacion;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
public interface INotificacionService {
    List<Notificacion> listar();
    Notificacion registrar(Reporte reporte, String tipo);
    Notificacion reintentar(Long id);
    void eliminarPorReporte(Long id);
}
