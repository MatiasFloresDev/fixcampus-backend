package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.UbicacionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class UbicacionController {
    private final UbicacionRepository repository;

    public UbicacionController(UbicacionRepository repository) { this.repository = repository; }

    @GetMapping
    @Operation(summary = "Listar ubicaciones", description = "Si se indica campus, busca ubicaciones cuyo campus contenga ese texto, sin distinguir mayúsculas.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<Ubicacion> listar(@Parameter(description = "Parte del nombre del campus") @RequestParam(required = false) String campus) {
        return campus == null || campus.isBlank() ? repository.findAll()
                : repository.findByCampusContainingIgnoreCase(campus);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public Ubicacion buscar(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ubicacion> crear(@Valid @RequestBody Ubicacion datos) {
        datos.setIdUbicacion(null);
        return ResponseEntity.status(201).body(repository.save(datos));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Ubicacion actualizar(@PathVariable Long id, @Valid @RequestBody Ubicacion datos) {
        Ubicacion actual = buscar(id);
        actual.setCampus(datos.getCampus());
        actual.setEdificio(datos.getEdificio());
        actual.setPiso(datos.getPiso());
        actual.setZona(datos.getZona());
        actual.setTipo(datos.getTipo());
        return repository.save(actual);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repository.delete(buscar(id));
        return ResponseEntity.noContent().build();
    }
}

