package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUbicacionService;
import pe.edu.upc.fixcampus.fixcampus.repositories.*;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import java.util.List;
import java.util.Optional;

@Service
public class UbicacionServiceImplement implements IUbicacionService {
    private final IUbicacionRepository repository;

    public UbicacionServiceImplement(IUbicacionRepository repository) { this.repository = repository; }

    public List<Ubicacion> listar() { return repository.findAll(); }

    public List<Ubicacion> buscarPorCampus(String campus) {
        return repository.findByCampusContainingIgnoreCase(campus);
    }

    public Ubicacion buscarPorId(Long id) {
        Optional<Ubicacion> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Ubicacion no encontrado");
        }
        return encontrado.get();
    }

    public Ubicacion registrar(Ubicacion datos) {
        datos.setIdUbicacion(null);
        return repository.save(datos);
    }

    public Ubicacion actualizar(Long id, Ubicacion datos) {
        Ubicacion actual = buscarPorId(id);
        actual.setCampus(datos.getCampus());
        actual.setEdificio(datos.getEdificio());
        actual.setPiso(datos.getPiso());
        actual.setZona(datos.getZona());
        actual.setTipo(datos.getTipo());
        return repository.save(actual);
    }

    public void eliminar(Long id) { repository.delete(buscarPorId(id)); }
}
