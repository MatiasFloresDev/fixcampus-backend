package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;
import pe.edu.upc.fixcampus.fixcampus.dtos.SugerenciaCategoriaDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.SugerenciaRecomendacionDTO;
public interface IIntegracionIAService {
    SugerenciaCategoriaDTO sugerirCategoria(String descripcion);
    SugerenciaRecomendacionDTO sugerirRecomendacion(Long reporteId);
}
