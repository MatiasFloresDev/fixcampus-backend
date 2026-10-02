package pe.edu.upc.fixcampus.fixcampus.controllers;

import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionesPorPrioridadDTO;
import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionDTOList;
import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionDTOInsert;
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
    public List<RecomendacionDTOList> listar() {
        return convertirLista(service.listar());
    }

    @GetMapping("/prioridad")
    @Operation(summary = "Buscar recomendaciones por prioridad", description = "Consulta 7, simple: filtra recomendaciones por prioridad sugerida. Ejemplo: MEDIA.")
    public List<RecomendacionDTOList> buscarPorPrioridad(@RequestParam String prioridad) {
        return convertirLista(service.buscarPorPrioridad(prioridad));
    }

    @GetMapping("/{id}")
    public RecomendacionDTOList buscar(@PathVariable Long id) { return convertir(service.buscarPorId(id)); }

    @PostMapping
    public ResponseEntity<RecomendacionDTOList> crear(@Valid @RequestBody RecomendacionDTOInsert datos) {
        Recomendacion guardado = service.registrar(datos);
        RecomendacionDTOList respuesta = convertir(guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    public RecomendacionDTOList actualizar(@PathVariable Long id, @Valid @RequestBody RecomendacionDTOInsert datos) {
        return convertir(service.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private RecomendacionDTOList convertir(Recomendacion recomendacion) {
        RecomendacionDTOList dto = new RecomendacionDTOList();
        dto.setIdRecomendacion(recomendacion.getIdRecomendacion());
        dto.setReporteId(recomendacion.getReporte().getIdReporte());
        dto.setTituloSugerido(recomendacion.getTituloSugerido());
        dto.setResumen(recomendacion.getResumen());
        dto.setPrioridadSugerida(recomendacion.getPrioridadSugerida());
        dto.setJustificacion(recomendacion.getJustificacion());
        dto.setFechaRecomendacion(recomendacion.getFechaRecomendacion());
        return dto;
    }

    private List<RecomendacionDTOList> convertirLista(List<Recomendacion> recomendaciones) {
        List<RecomendacionDTOList> lista = new ArrayList<>();
        for (Recomendacion recomendacion : recomendaciones) {
            lista.add(convertir(recomendacion));
        }
        return lista;
    }
    @GetMapping("/estadisticas/por-prioridad-reporte")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Contar recomendaciones por prioridad de incidencia", description = "Consulta 8: un JOIN y COUNT. Cuenta recomendaciones según la prioridad de la incidencia asociada. No pide parámetros. Solo administradores.")
    public List<RecomendacionesPorPrioridadDTO> contarPorPrioridadReporte() {
        return service.contarPorPrioridadReporte();
    }
}
