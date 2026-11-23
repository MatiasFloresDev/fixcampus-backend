package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;

import java.util.List;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByDescripcionContainingIgnoreCase(String descripcion);
}
