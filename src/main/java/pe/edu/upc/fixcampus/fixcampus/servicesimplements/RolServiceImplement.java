package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IRolService;
import pe.edu.upc.fixcampus.fixcampus.repositories.*;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImplement implements IRolService {
    private final IRolRepository repository;

    public RolServiceImplement(IRolRepository repository) { this.repository = repository; }

    public List<Rol> listar() { return repository.findAll(); }


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
