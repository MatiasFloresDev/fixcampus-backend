package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.ReporteService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReporteController {

    private final ReporteService service;

    public ReporteController(ReporteService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar reportes", description = "Sin filtros lista todos. Con estado filtra por estado; con categoria busca reportes de esa categoría; con correo busca los creados por ese usuario. Si se envían varios filtros, se aplica primero estado, luego categoria y luego correo.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<ReporteDTOList>> listar(
            @Parameter(description = "Estado del reporte, por ejemplo ABIERTO") @RequestParam(required = false) String estado,
            @Parameter(description = "Nombre exacto de la categoría; consulta con JOIN") @RequestParam(required = false) String categoria,
            @Parameter(description = "Correo exacto del usuario reportante; consulta con JOIN") @RequestParam(required = false) String correo) {

        List<Reporte> reportes;
        if (estado != null && !estado.isBlank()) {
            reportes = service.buscarPorEstado(estado);
        } else if (categoria != null && !categoria.isBlank()) {
            reportes = service.buscarPorCategoria(categoria);
        } else if (correo != null && !correo.isBlank()) {
            reportes = service.buscarPorCorreoReportante(correo);
        } else {
            reportes = service.listar();
        }

        return ResponseEntity.ok(reportes.stream().map(this::convertirDto).toList());
    }

    @GetMapping("/estadisticas/por-usuario-mes")
    @Operation(summary = "Contar incidencias por usuario y mes", description = "Agrupa los reportes por usuario, año y mes de creación, y cuenta cuántos hizo cada uno. Consulta con JOIN. Solo para administradores.")
    @PreAuthorize("hasRole('ADMIN')")
    public List<IncidenciasPorMesDTO> incidenciasPorUsuarioYMes() {
        return service.contarPorUsuarioYMes();
    }

    @GetMapping("/estadisticas/por-campus")
    @Operation(summary = "Contar incidencias por campus y estado", description = "Une reportes con ubicaciones y cuenta cuántos reportes del estado indicado hay en cada campus. Solo para administradores.")
    @PreAuthorize("hasRole('ADMIN')")
    public List<IncidenciasPorCampusDTO> incidenciasPorCampus(
            @Parameter(description = "Estado del reporte, por ejemplo ABIERTO") @RequestParam String estado) {
        return service.contarPorCampusYEstado(estado);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ReporteDTOList> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertirDto(service.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ReporteDTOList> registrar(@Valid @RequestBody ReporteDTOInsert dto) {
        Reporte guardado = service.registrar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getIdReporte())
                .toUri();
        return ResponseEntity.created(location).body(convertirDto(guardado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ReporteDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReporteDTOInsert dto) {
        return ResponseEntity.ok(convertirDto(service.actualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<List<ReporteDTOList>> buscarPorPrioridad(
            @PathVariable String prioridad) {

        List<Reporte> reportes =
                service.buscarPorPrioridad(prioridad);

        List<ReporteDTOList> resultado = reportes.stream()
                .map(this::convertirDto)
                .toList();

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/campus/{campus}")
    public ResponseEntity<List<ReporteDTOList>> buscarPorCampus(
            @PathVariable String campus) {

        List<Reporte> reportes =
                service.buscarPorCampus(campus);

        List<ReporteDTOList> resultado = reportes.stream()
                .map(this::convertirDto)
                .toList();

        return ResponseEntity.ok(resultado);
    }

    private ReporteDTOList convertirDto(Reporte reporte) {
        ReporteDTOList dto = new ReporteDTOList();
        dto.setIdReporte(reporte.getIdReporte());
        dto.setUsuarioReportanteId(reporte.getUsuarioReportante().getIdUsuario());
        dto.setTecnicoAsignadoId(reporte.getTecnicoAsignado() == null ? null : reporte.getTecnicoAsignado().getIdUsuario());
        dto.setCategoriaId(reporte.getCategoria().getIdCategoria());
        dto.setCategoriaNombre(reporte.getCategoria().getNombre());
        dto.setUbicacionId(reporte.getUbicacion().getIdUbicacion());
        dto.setTitulo(reporte.getTitulo());
        dto.setDescripcion(reporte.getDescripcion());
        dto.setDetalleUbicacion(reporte.getDetalleUbicacion());
        dto.setPrioridad(reporte.getPrioridad());
        dto.setEstado(reporte.getEstado());
        dto.setFechaCreacion(reporte.getFechaCreacion());
        dto.setFechaAsignacion(reporte.getFechaAsignacion());
        dto.setFechaResolucion(reporte.getFechaResolucion());
        return dto;
    }
}



