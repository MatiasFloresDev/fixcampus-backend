package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.RolService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    private final RolService service;

    public RolController(RolService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Listar roles", description = "Si se indica nombre, busca roles que contengan ese texto, sin distinguir mayúsculas.")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Rol> listar(@Parameter(description = "Parte del nombre del rol") @RequestParam(required = false) String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return service.listar();
        } else {
            return service.buscarPorNombre(nombre);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Rol buscar(@PathVariable Long id) { return service.buscarPorId(id); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Rol> crear(@Valid @RequestBody Rol datos) {
        return ResponseEntity.status(201).body(service.registrar(datos));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Rol actualizar(@PathVariable Long id, @Valid @RequestBody Rol datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
