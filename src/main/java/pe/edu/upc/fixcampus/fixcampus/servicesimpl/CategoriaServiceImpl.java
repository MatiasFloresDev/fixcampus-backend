package pe.edu.upc.fixcampus.fixcampus.servicesimpl;
import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.dtos.CategoriaConReportesDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.CategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.CategoriaService;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
@Service
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository repository;
    public CategoriaServiceImpl(CategoriaRepository repository) {

        this.repository = repository;
    }
    public List<Categoria> listar(){

        return repository.findAll();
    }
    public List<Categoria> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }
    public List<Categoria> buscarPorDescripcion(String palabraClave) {
        return repository.buscarPorDescripcion(palabraClave);
    }

    // Convierte las columnas de la consulta 20 a su DTO de salida.
    public List<CategoriaConReportesDTO> contarReportesPorCategoria() {
        List<CategoriaConReportesDTO> lista = new ArrayList<>();
        for (Object[] fila : repository.contarReportesPorCategoria()) {
            CategoriaConReportesDTO dto = new CategoriaConReportesDTO(
                    ((Number) fila[0]).longValue(), (String) fila[1],
                    (String) fila[2], ((Number) fila[3]).longValue());
            lista.add(dto);
        }
        return lista;
    }
    public Categoria buscarPorId(Long id) {
        Optional<Categoria> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Categoría no encontrada");
        }
        return encontrado.get();
    }

   public Categoria registrar(Categoria categoria){

        return repository.save(categoria);
   }

   public Categoria actualizar(Long id,Categoria datos){
        Categoria actual= buscarPorId(id);

        actual.setNombre(datos.getNombre());
        actual.setDescripcion(datos.getDescripcion());

        return repository.save(actual);
   }

    public void eliminar(Long id) {
        Categoria actual = buscarPorId(id);
        repository.delete(actual);
    }
}
