package pe.edu.upc.fixcampus.fixcampus.servicesimplements;
import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.ICategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.ICategoriaService;

import java.util.List;
import java.util.Optional;
@Service
public class CategoriaServiceImplement implements ICategoriaService {
    private final ICategoriaRepository repository;
    public CategoriaServiceImplement(ICategoriaRepository repository) {

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
