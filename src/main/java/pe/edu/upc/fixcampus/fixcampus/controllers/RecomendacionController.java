package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.RecomendacionRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.ReporteRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
@PreAuthorize("hasRole('ADMIN')")
public class RecomendacionController {
    private final RecomendacionRepository repository;
    private final ReporteRepository reporteRepository;

    public RecomendacionController(RecomendacionRepository repository,
                                     ReporteRepository reporteRepository) {
        this.repository = repository;
        this.reporteRepository = reporteRepository;
    }

    @GetMapping
    public List<RecomendacionDTO> listar(@RequestParam(required = false) String prioridad) {
        List<Recomendacion> lista = prioridad == null || prioridad.isBlank()
                ? repository.findAll()
                : repository.findByPrioridadSugeridaIgnoreCase(prioridad);
        return lista.stream().map(this::convertir).toList();
    }

      @GetMapping("/por-categoria")
    public List<RecomendacionDTO> listarPorCategoria(@RequestParam String nombre) {
        return repository.findByCategoriaDelReporte(nombre)
                .stream().map(this::convertir).toList();
    }

    @GetMapping("/{id}")
    public RecomendacionDTO buscar(@PathVariable Long id) { return convertir(buscarEntidad(id)); }

    @PostMapping
    public ResponseEntity<RecomendacionDTO> crear(@Valid @RequestBody RecomendacionDTO datos) {
        if (repository.existsByReporte_IdReporte(datos.getReporteId())) {
            throw new IllegalArgumentException("Este reporte ya tiene una recomendación");
        }
        Recomendacion recomendacion = new Recomendacion();
        copiarDatos(recomendacion, datos);
        recomendacion.setFechaRecomendacion(LocalDateTime.now());
        return ResponseEntity.status(201).body(convertir(repository.save(recomendacion)));
    }

    @PutMapping("/{id}")
    public RecomendacionDTO actualizar(@PathVariable Long id,
                                         @Valid @RequestBody RecomendacionDTO datos) {
        Recomendacion recomendacion = buscarEntidad(id);
        if (!recomendacion.getReporte().getIdReporte().equals(datos.getReporteId())
                && repository.existsByReporte_IdReporte(datos.getReporteId())) {
            throw new IllegalArgumentException("Este reporte ya tiene una recomendación");
        }
        copiarDatos(recomendacion, datos);
        return convertir(repository.save(recomendacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repository.delete(buscarEntidad(id));
        return ResponseEntity.noContent().build();
    }

    private Recomendacion buscarEntidad(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recomendación no encontrada"));
    }

    private void copiarDatos(Recomendacion recomendacion, RecomendacionDTO datos) {
        recomendacion.setReporte(reporteRepository.findById(datos.getReporteId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado")));
        recomendacion.setTituloSugerido(datos.getTituloSugerido());
        recomendacion.setResumen(datos.getResumen());
        recomendacion.setPrioridadSugerida(datos.getPrioridadSugerida());
        recomendacion.setJustificacion(datos.getJustificacion());
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
}
