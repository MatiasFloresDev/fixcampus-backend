package pe.edu.upc.fixcampus.fixcampus.servicesimpl;
import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.CategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.CategoriaService;

import java.util.List;
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
    public Categoria buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada"
                ));
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
