package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IRecomendacionService;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IReporteService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
@PreAuthorize("hasRole('ADMIN')")
public class RecomendacionController {

    private final IRecomendacionService recomendacionService;
    private final IReporteService reporteService;
    private final ModelMapper modelMapper;

    public RecomendacionController(
            IRecomendacionService recomendacionService,
            IReporteService reporteService,
            ModelMapper modelMapper) {

        this.recomendacionService = recomendacionService;
        this.reporteService = reporteService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<RecomendacionDTOList>> listar() {

        List<RecomendacionDTOList> lista =
                recomendacionService.listar()
                        .stream()
                        .map(recomendacion ->
                                modelMapper.map(
                                        recomendacion,
                                        RecomendacionDTOList.class
                                )
                        )
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/prioridad")
    public ResponseEntity<List<RecomendacionDTOList>> buscarPorPrioridad(
            @RequestParam String prioridad) {

        List<RecomendacionDTOList> lista =
                recomendacionService.buscarPorPrioridad(prioridad)
                        .stream()
                        .map(recomendacion ->
                                modelMapper.map(
                                        recomendacion,
                                        RecomendacionDTOList.class
                                )
                        )
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/por-categoria")
    public ResponseEntity<List<RecomendacionDTOList>> listarPorCategoria(
            @RequestParam String nombre) {

        List<RecomendacionDTOList> lista =
                recomendacionService.buscarPorCategoria(nombre)
                        .stream()
                        .map(recomendacion ->
                                modelMapper.map(
                                        recomendacion,
                                        RecomendacionDTOList.class
                                )
                        )
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecomendacionDTOList> buscar(
            @PathVariable Long id) {

        Recomendacion recomendacion =
                recomendacionService.buscarPorId(id);

        RecomendacionDTOList dto =
                modelMapper.map(
                        recomendacion,
                        RecomendacionDTOList.class
                );

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<RecomendacionDTOList> registrar(
            @Valid @RequestBody RecomendacionDTOInsert dto) {

        Reporte reporte =
                reporteService.buscarPorId(
                        dto.getReporteId()
                );

        Recomendacion recomendacion =
                modelMapper.map(
                        dto,
                        Recomendacion.class
                );

        recomendacion.setReporte(reporte);

        Recomendacion guardada =
                recomendacionService.registrar(recomendacion);

        RecomendacionDTOList responseDTO =
                modelMapper.map(
                        guardada,
                        RecomendacionDTOList.class
                );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(
                        guardada.getIdRecomendacion()
                )
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecomendacionDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecomendacionDTOInsert dto) {

        Recomendacion recomendacion =
                modelMapper.map(
                        dto,
                        Recomendacion.class
                );

        Recomendacion actualizada =
                recomendacionService.actualizar(
                        id,
                        recomendacion
                );

        RecomendacionDTOList responseDTO =
                modelMapper.map(
                        actualizada,
                        RecomendacionDTOList.class
                );

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        recomendacionService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}