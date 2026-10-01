package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;

import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IRecomendacionService;
import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
@PreAuthorize("hasRole('ADMIN')")
public class RecomendacionController {
    private final IRecomendacionService service;

    public RecomendacionController(IRecomendacionService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Listar recomendaciones", description = "Devuelve todas las recomendaciones de mantenimiento sin pedir parámetros.")
    public List<RecomendacionDTO> listar() {
        return convertirLista(service.listar());
    }

    @GetMapping("/prioridad")
    @Operation(summary = "Buscar recomendaciones por prioridad", description = "Consulta 16: filtra recomendaciones por prioridad sugerida. Ejemplo: MEDIA.")
    public List<RecomendacionDTO> buscarPorPrioridad(@RequestParam String prioridad) {
        return convertirLista(service.buscarPorPrioridad(prioridad));
    }

    @GetMapping("/por-categoria")
    @Operation(summary = "Buscar recomendaciones por categoría", description = "Consulta 17 con JOIN: une recomendación, reporte y categoría. Ejemplo: Electricidad.")
    public List<RecomendacionDTO> listarPorCategoria(@RequestParam String nombre) {
        return convertirLista(service.buscarPorCategoria(nombre));
    }

    @GetMapping("/{id}")
    public RecomendacionDTO buscar(@PathVariable Long id) { return convertir(service.buscarPorId(id)); }

    @PostMapping
    public ResponseEntity<RecomendacionDTO> crear(@Valid @RequestBody RecomendacionDTO datos) {
        return ResponseEntity.status(201).body(convertir(service.registrar(datos)));
    }

    @PutMapping("/{id}")
    public RecomendacionDTO actualizar(@PathVariable Long id, @Valid @RequestBody RecomendacionDTO datos) {
        return convertir(service.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private RecomendacionDTO convertir(Recomendacion recomendacion) {
        RecomendacionDTO dto = new RecomendacionDTO();
        dto.setIdRecomendacion(recomendacion.getIdRecomendacion());
        dto.setReporteId(recomendacion.getReporte().getIdReporte());
        dto.setTituloSugerido(recomendacion.getTituloSugerido());
        dto.setResumen(recomendacion.getResumen());
        dto.setPrioridadSugerida(recomendacion.getPrioridadSugerida());
        dto.setJustificacion(recomendacion.getJustificacion());
        dto.setFechaRecomendacion(recomendacion.getFechaRecomendacion());
        return dto;
    }

    private List<RecomendacionDTO> convertirLista(List<Recomendacion> recomendaciones) {
        List<RecomendacionDTO> lista = new ArrayList<>();
        for (Recomendacion recomendacion : recomendaciones) {
            lista.add(convertir(recomendacion));
        }
        return lista;
    }
}
