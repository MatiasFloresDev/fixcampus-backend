package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.RolRepository;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    private final RolRepository repository;

    public RolController(RolRepository repository) { this.repository = repository; }

    @GetMapping
    @Operation(summary = "Listar roles", description = "Si se indica nombre, busca roles que contengan ese texto, sin distinguir mayúsculas.")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Rol> listar(@Parameter(description = "Parte del nombre del rol") @RequestParam(required = false) String nombre) {
        return nombre == null || nombre.isBlank() ? repository.findAll()
                : repository.findByNombreContainingIgnoreCase(nombre);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Rol buscar(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Rol> crear(@Valid @RequestBody Rol datos) {
        datos.setIdRol(null);
        return ResponseEntity.status(201).body(repository.save(datos));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Rol actualizar(@PathVariable Long id, @Valid @RequestBody Rol datos) {
        Rol actual = buscar(id);
        actual.setNombre(datos.getNombre());
        actual.setNivelAcceso(datos.getNivelAcceso());
        actual.setDescripcion(datos.getDescripcion());
        return repository.save(actual);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repository.delete(buscar(id));
        return ResponseEntity.noContent().build();
    }
}

