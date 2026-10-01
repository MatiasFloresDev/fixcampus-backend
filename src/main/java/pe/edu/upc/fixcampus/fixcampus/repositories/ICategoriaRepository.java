package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    // Consulta 1: busca categorías por parte del nombre, sin distinguir mayúsculas.
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    Optional<Categoria> findByNombreIgnoreCase(String nombre);

    // Consulta 15: busca categorías por una palabra de su descripción.
    @Query("select c from Categoria c where lower(c.descripcion) like lower(concat('%', :palabraClave, '%'))")
    List<Categoria> buscarPorDescripcion(@Param("palabraClave") String palabraClave);
}
