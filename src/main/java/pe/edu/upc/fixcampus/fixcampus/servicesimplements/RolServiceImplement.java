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

    public RolServiceImplement(IRolRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Rol> listar() {
        return repository.findAll();
    }

    @Override
    public Rol buscarPorId(Long id) {
        Optional<Rol> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Rol no encontrado");
        }
        return encontrado.get();
    }

    @Override
    public Rol registrar(Rol datos) {
        datos.setIdRol(null);
        return repository.save(datos);
    }

    @Override
    public Rol actualizar(Long id, Rol datos) {
        Rol actual = buscarPorId(id);
        if (esRolBase(actual) && !actual.getNombre().equals(datos.getNombre())) {
            throw new IllegalArgumentException("No se puede cambiar el nombre de un rol del sistema.");
        }
        actual.setNombre(datos.getNombre());
        actual.setNivelAcceso(datos.getNivelAcceso());
        actual.setDescripcion(datos.getDescripcion());
        return repository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Rol rol = buscarPorId(id);
        if (esRolBase(rol)) {
            throw new IllegalArgumentException("No se puede eliminar un rol del sistema.");
        }
        repository.delete(rol);
    }

    private boolean esRolBase(Rol rol) {
        return "ADMIN".equals(rol.getNombre()) || "USUARIO".equals(rol.getNombre());
    }
}
