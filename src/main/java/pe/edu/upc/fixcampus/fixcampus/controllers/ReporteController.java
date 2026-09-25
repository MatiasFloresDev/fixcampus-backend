package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.ReporteService;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReporteController {

    private final ReporteService service;
    private final UsuarioRepository usuarioRepository;

    public ReporteController(ReporteService service, UsuarioRepository usuarioRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/mis-reportes")
    @Operation(summary = "Listar mis reportes", description = "Muestra solo las incidencias registradas por la cuenta que inició sesión.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<ReporteDTOList> misReportes(Authentication authentication) {
        return service.buscarPorCorreoReportante(authentication.getName())
                .stream().map(this::convertirDto).toList();
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
    public ResponseEntity<ReporteDTOList> registrar(@Valid @RequestBody ReporteDTOInsert dto,
                                                    Authentication authentication) {
        Long idUsuario = usuarioRepository.findByCorreo(authentication.getName())
                .orElseThrow().getIdUsuario();
        dto.setUsuarioReportanteId(idUsuario);
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
            @Valid @RequestBody ReporteDTOInsert dto,
            Authentication authentication) {
        Reporte existente = service.buscarPorId(id);
        boolean administrador = authentication.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority().equals("ROLE_ADMIN"));
        boolean esPropietario = existente.getUsuarioReportante().getCorreo()
                .equalsIgnoreCase(authentication.getName());
        if (!administrador && !esPropietario) {
            throw new AccessDeniedException("Solo puedes editar tus propios reportes");
        }
        if (!administrador) {
            dto.setUsuarioReportanteId(existente.getUsuarioReportante().getIdUsuario());
            dto.setTecnicoAsignadoId(existente.getTecnicoAsignado() == null ? null : existente.getTecnicoAsignado().getIdUsuario());
            dto.setEstado(existente.getEstado());
        }
        return ResponseEntity.ok(convertirDto(service.actualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
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
