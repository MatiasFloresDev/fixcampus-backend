package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import java.util.List;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;

public interface ICategoriaService {
    List<Categoria> listar();

    List<Categoria> buscarPorDescripcion(String palabraClave);

    List<Object[]> contarReportesPorCategoria();

    Categoria buscarPorId(Long id);
    Categoria registrar(Categoria category);
    Categoria actualizar(Long id, Categoria datos);
    void eliminar(Long id);
}
