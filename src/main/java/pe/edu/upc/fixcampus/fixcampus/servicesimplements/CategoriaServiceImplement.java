package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.ICategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.ICategoriaService;

import java.util.List;

@Service
public class CategoriaServiceImplement implements ICategoriaService {

    private final ICategoriaRepository categoriaRepository;

    public CategoriaServiceImplement(ICategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @Override
    public List<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Categoria> buscarPorDescripcion(String palabraClave) {
        return categoriaRepository.buscarPorDescripcion(palabraClave);
    }

    @Override
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada"));
    }

    @Override
    public Categoria registrar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria actualizar(Long id, Categoria datos) {
        Categoria actual = buscarPorId(id);

        actual.setNombre(datos.getNombre());
        actual.setDescripcion(datos.getDescripcion());

        return categoriaRepository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Categoria actual = buscarPorId(id);
        categoriaRepository.delete(actual);
    }
}
