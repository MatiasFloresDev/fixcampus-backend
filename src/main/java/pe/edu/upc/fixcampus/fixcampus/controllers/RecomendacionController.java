package pe.edu.upc.fixcampus.fixcampus.controllers;

import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionesPorPrioridadDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final IRecomendacionService service;

    public RecomendacionController(IRecomendacionService service, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar recomendaciones", description = "Devuelve todas las recomendaciones de mantenimiento sin pedir parámetros.")
    public ResponseEntity<List<RecomendacionDTOList>> listar() {
        return ResponseEntity.ok(convertirLista(service.listar()));
    }

    @GetMapping("/prioridad")
    @Operation(summary = "Buscar recomendaciones por prioridad", description = "Consulta 7, simple: filtra recomendaciones por prioridad sugerida. Ejemplo: MEDIA.")
    public ResponseEntity<List<RecomendacionDTOList>> buscarPorPrioridad(@RequestParam String prioridad) {
        return ResponseEntity.ok(convertirLista(service.buscarPorPrioridad(prioridad)));
    }

    @GetMapping("/reporte/{reporteId}")
    @Operation(summary = "Obtener la recomendación asociada al reporte")
    public ResponseEntity<RecomendacionDTOList> buscarPorReporte(@PathVariable Long reporteId) {
        return service.buscarPorReporte(reporteId)
                .map(recomendacion -> ResponseEntity.ok(convertir(recomendacion)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecomendacionDTOList> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(convertir(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<RecomendacionDTOList> crear(@Valid @RequestBody RecomendacionDTOInsert datos) {
        Recomendacion guardado = service.registrar(datos);
        RecomendacionDTOList respuesta = convertir(guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecomendacionDTOList> actualizar(@PathVariable Long id, @Valid @RequestBody RecomendacionDTOInsert datos) {
        return ResponseEntity.ok(convertir(service.actualizar(id, datos)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private RecomendacionDTOList convertir(Recomendacion recomendacion) {
        RecomendacionDTOList dto = modelMapper.map(recomendacion, RecomendacionDTOList.class);
        dto.setReporteId(recomendacion.getReporte().getIdReporte());
        return dto;
    }

    private List<RecomendacionDTOList> convertirLista(List<Recomendacion> recomendaciones) {
        return recomendaciones.stream()
                .map(item -> convertir(item))
                .toList();
    }
    @GetMapping("/estadisticas/por-prioridad-reporte")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Contar recomendaciones por prioridad de incidencia", description = "Consulta 8: un JOIN y COUNT. Cuenta recomendaciones según la prioridad de la incidencia asociada. No pide parámetros. Solo administradores.")
    public ResponseEntity<List<RecomendacionesPorPrioridadDTO>> contarPorPrioridadReporte() {
        return ResponseEntity.ok(service.contarPorPrioridadReporte());
    }
}
