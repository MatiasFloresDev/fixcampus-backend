package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.ICategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.ICategoriaService;

@Service
public class CategoriaServiceImplement implements ICategoriaService {

    private final ICategoriaRepository repository;

    public CategoriaServiceImplement(ICategoriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Categoria> listar() {
        return repository.findAll();
    }

    @Override
    public List<Categoria> buscarPorDescripcion(String palabraClave) {
        return repository.buscarPorDescripcion(palabraClave);
    }

    @Override
    public List<Object[]> contarReportesPorCategoria() {
        return repository.contarReportesPorCategoria();
    }

    @Override
    public Categoria buscarPorId(Long id) {
        Optional<Categoria> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Categoría no encontrada");
        }
        return encontrado.get();
    }

    public Categoria registrar(Categoria categoria) {
        validar(categoria);
        return repository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria datos) {
        validar(datos);
        Categoria actual = buscarPorId(id);

        actual.setNombre(datos.getNombre());
        actual.setDescripcion(datos.getDescripcion());

        return repository.save(actual);
    }

    private void validar(Categoria categoria) {
        String nombre = categoria.getNombre().trim();
        if (nombre.length() < 3 || nombre.length() > 100) {
            throw new IllegalArgumentException(
                "El nombre debe tener entre 3 y 100 caracteres sin contar espacios al inicio o al final"
            );
        }
        categoria.setNombre(nombre);
        categoria.setDescripcion(categoria.getDescripcion().trim());
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
