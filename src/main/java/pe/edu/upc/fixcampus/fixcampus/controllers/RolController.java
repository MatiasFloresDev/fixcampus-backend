package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IRolService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    private final IRolService service;

    public RolController(IRolService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Listar roles", description = "Muestra todos los roles sin parámetros. Solo administradores.")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Rol> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Rol buscar(@PathVariable Long id) { return service.buscarPorId(id); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Rol> crear(@Valid @RequestBody Rol datos) {
        Rol guardado = service.registrar(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
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
