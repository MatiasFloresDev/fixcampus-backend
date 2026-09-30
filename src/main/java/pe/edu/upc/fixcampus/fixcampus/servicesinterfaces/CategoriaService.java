package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.dtos.CategoriaConReportesDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import java.util.List;

public interface CategoriaService {

    List<Categoria> listar();

    List<Categoria> buscarPorNombre(String nombre);
    List<Categoria> buscarPorDescripcion(String palabraClave);

    // Query 2: JOIN con Reporte y COUNT
    List<CategoriaConReportesDTO> contarReportesPorCategoria();

    Categoria buscarPorId(Long id);
    Categoria registrar(Categoria category);
    Categoria actualizar(Long id, Categoria datos);
    void eliminar(Long id);
}