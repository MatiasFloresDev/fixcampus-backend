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

    public UbicacionServiceImplement(IUbicacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Ubicacion> listar() {
        return repository.findAll();
    }

    @Override
    public Ubicacion buscarPorId(Long id) {
        Optional<Ubicacion> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ResourceNotFoundException("Ubicacion no encontrado");
        }
        return encontrado.get();
    }

    @Override
    public Ubicacion registrar(Ubicacion datos) {
        validarCoordenadas(datos);
        datos.setIdUbicacion(null);
        return repository.save(datos);
    }

    @Override
    public Ubicacion actualizar(Long id, Ubicacion datos) {
        Ubicacion actual = buscarPorId(id);
        validarCoordenadas(datos);
        actual.setCampus(datos.getCampus());
        actual.setEdificio(datos.getEdificio());
        actual.setPiso(datos.getPiso());
        actual.setZona(datos.getZona());
        actual.setTipo(datos.getTipo());
        actual.setLatitud(datos.getLatitud());
        actual.setLongitud(datos.getLongitud());
        return repository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        repository.delete(buscarPorId(id));
    }

    private void validarCoordenadas(Ubicacion ubicacion) {
        if ((ubicacion.getLatitud() == null) != (ubicacion.getLongitud() == null)) {
            throw new IllegalArgumentException("Ingresa latitud y longitud juntas.");
        }
    }
}
