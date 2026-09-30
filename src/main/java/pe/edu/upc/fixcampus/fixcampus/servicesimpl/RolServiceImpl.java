package pe.edu.upc.fixcampus.fixcampus.servicesimpl;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.RolService;
import pe.edu.upc.fixcampus.fixcampus.repositories.*;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {
    private final RolRepository repository;

    public RolServiceImpl(RolRepository repository) { this.repository = repository; }

    public List<Rol> listar() { return repository.findAll(); }

    public List<Rol> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public Rol buscarPorId(Long id) {
        Optional<Rol> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Rol no encontrado");
        }
        return encontrado.get();
    }

    public Rol registrar(Rol datos) {
        datos.setIdRol(null);
        return repository.save(datos);
    }

    public Rol actualizar(Long id, Rol datos) {
        Rol actual = buscarPorId(id);
        actual.setNombre(datos.getNombre());
        actual.setNivelAcceso(datos.getNivelAcceso());
        actual.setDescripcion(datos.getDescripcion());
        return repository.save(actual);
    }

    public void eliminar(Long id) { repository.delete(buscarPorId(id)); }
}
