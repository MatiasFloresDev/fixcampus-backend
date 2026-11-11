package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;

import java.util.List;

@Repository
public interface IUbicacionRepository extends JpaRepository<Ubicacion, Long> {
    List<Ubicacion> findByCampusContainingIgnoreCase(String campus);
}
