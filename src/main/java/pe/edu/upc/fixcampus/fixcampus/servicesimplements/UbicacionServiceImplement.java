package pe.edu.upc.fixcampus.fixcampus.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUbicacionRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUbicacionService;

import java.util.List;

@Service
public class UbicacionServiceImplement implements IUbicacionService {

    private final IUbicacionRepository ubicacionRepository;

    public UbicacionServiceImplement(IUbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public List<Ubicacion> listar() {
        return ubicacionRepository.findAll();
    }

    @Override
    public List<Ubicacion> buscarPorCampus(String campus) {
        return ubicacionRepository.findByCampusContainingIgnoreCase(campus);
    }

    @Override
    public Ubicacion buscarPorId(Long id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ubicación no encontrada"));
    }

    @Override
    public Ubicacion registrar(Ubicacion datos) {
        return ubicacionRepository.save(datos);
    }

    @Override
    public Ubicacion actualizar(Long id, Ubicacion datos) {
        Ubicacion actual = buscarPorId(id);

        actual.setCampus(datos.getCampus());
        actual.setEdificio(datos.getEdificio());
        actual.setPiso(datos.getPiso());
        actual.setZona(datos.getZona());
        actual.setTipo(datos.getTipo());

        return ubicacionRepository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Ubicacion actual = buscarPorId(id);
        ubicacionRepository.delete(actual);
    }
}