package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.IRolRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IRolService;

import java.util.List;

@Service
public class RolServiceImplement implements IRolService {

    private final IRolRepository rolRepository;

    public RolServiceImplement(IRolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public List<Rol> listar() {
        return rolRepository.findAll();
    }

    @Override
    public List<Rol> buscarPorNombre(String nombre) {
        return rolRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Rol buscarPorNombreExacto(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Rol no encontrado"));
    }
    @Override
    public Rol buscarPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado"));
    }

    @Override
    public Rol registrar(Rol datos) {
        return rolRepository.save(datos);
    }

    @Override
    public Rol actualizar(Long id, Rol datos) {
        Rol actual = buscarPorId(id);

        actual.setNombre(datos.getNombre());
        actual.setNivelAcceso(datos.getNivelAcceso());
        actual.setDescripcion(datos.getDescripcion());

        return rolRepository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Rol actual = buscarPorId(id);
        rolRepository.delete(actual);
    }
}