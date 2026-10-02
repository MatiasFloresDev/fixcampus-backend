package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUbicacionService;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class UbicacionController {
    private final IUbicacionService service;

    public UbicacionController(IUbicacionService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Listar ubicaciones", description = "Consulta 3, simple: busca ubicaciones por una parte del nombre del campus. Sin campus lista todas. Ejemplo: Monterrico.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<Ubicacion> listar(@Parameter(description = "Parte del nombre del campus") @RequestParam(required = false) String campus) {
        if (campus == null || campus.isBlank()) {
            return service.listar();
        } else {
            return service.buscarPorCampus(campus);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public Ubicacion buscar(@PathVariable Long id) { return service.buscarPorId(id); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ubicacion> crear(@Valid @RequestBody Ubicacion datos) {
        Ubicacion guardado = service.registrar(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Ubicacion actualizar(@PathVariable Long id, @Valid @RequestBody Ubicacion datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
