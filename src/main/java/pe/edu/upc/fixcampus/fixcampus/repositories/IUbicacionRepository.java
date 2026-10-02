package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;

@Repository
public interface IUbicacionRepository extends JpaRepository<Ubicacion, Long> {
}
