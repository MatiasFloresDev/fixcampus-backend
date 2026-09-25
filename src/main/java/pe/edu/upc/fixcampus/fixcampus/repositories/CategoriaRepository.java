package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Consulta 1: busca categorías por parte del nombre, sin distinguir mayúsculas.
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    Optional<Categoria> findByNombreIgnoreCase(String nombre);
}
