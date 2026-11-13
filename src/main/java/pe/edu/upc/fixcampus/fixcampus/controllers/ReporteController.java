package pe.edu.upc.fixcampus.fixcampus.controllers;

import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IReporteService;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReporteController {

    private final ModelMapper modelMapper;
    private final IReporteService service;
    private final IUsuarioService usuarioService;

    public ReporteController(IReporteService service, IUsuarioService usuarioService, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/mis-reportes")
    @Operation(summary = "Listar mis reportes", description = "Muestra solo las incidencias registradas por la cuenta que inició sesión.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<ReporteDTOList>> misReportes(Authentication authentication) {
        return ResponseEntity.ok(convertirLista(service.buscarPorReportante(idUsuario(authentication))));
    }

    //matias
    @GetMapping
    @Operation(summary = "Listar reportes por estado", description = "El administrador consulta todos los reportes y el usuario solo los propios. Filtro opcional, por ejemplo ABIERTO.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<ReporteDTOList>> listar(
            @Parameter(description = "Estado del reporte, por ejemplo ABIERTO") @RequestParam(required = false) String estado,
            Authentication authentication) {
        if (!esAdministrador(authentication)) {
            List<Reporte> propios = estado == null || estado.isBlank()
                    ? service.buscarPorReportante(idUsuario(authentication))
                    : service.buscarPropiosPorEstado(idUsuario(authentication), estado);
            return ResponseEntity.ok(convertirLista(propios));
        }
        List<Reporte> reportes;
        if (estado != null && !estado.isBlank()) {
            reportes = service.buscarPorEstado(estado);
        } else {
            reportes = service.listar();
        }
        return ResponseEntity.ok(convertirLista(reportes));
    }
    //matias
    @GetMapping("/estadisticas/por-usuario-mes")
    @Operation(summary = "Contar incidencias por usuario y mes", description = "Consulta 2: JOIN, GROUP BY y COUNT. Cuenta incidencias por usuario, año y mes. Solo administradores.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<IncidenciasPorMesDTO>> incidenciasPorUsuarioYMes() {
        return ResponseEntity.ok(service.contarPorUsuarioYMes());
    }

    @GetMapping("/prioridad/{prioridad}")
    @Operation(summary = "Buscar reportes por prioridad", description = "Consulta 3, simple: lista las incidencias de la prioridad indicada. Ejemplo: ALTA.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<ReporteDTOList>> buscarPorPrioridad(@PathVariable String prioridad,
                                                                  Authentication authentication) {
        List<Reporte> reportes = esAdministrador(authentication)
                ? service.buscarPorPrioridad(prioridad)
                : service.buscarPropiosPorPrioridad(idUsuario(authentication), prioridad);
        return ResponseEntity.ok(convertirLista(reportes));
    }

    @GetMapping("/campus/{campus}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar reportes por campus")
    public ResponseEntity<List<ReporteDTOList>> buscarPorCampus(@PathVariable String campus) {
        return ResponseEntity.ok(convertirLista(service.buscarPorCampus(campus)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ReporteDTOList> buscarPorId(@PathVariable Long id, Authentication authentication) {
        Reporte reporte = service.buscarPorId(id);
        verificarAcceso(reporte, authentication);
        return ResponseEntity.ok(convertirDto(reporte));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ReporteDTOList> registrar(@Valid @RequestBody ReporteDTOInsert dto,
                                                    Authentication authentication) {
        Long idUsuario = idUsuario(authentication);
        if (!usuarioService.existe(idUsuario)) {
            throw new AccessDeniedException("Usuario autenticado no encontrado");
        }
        dto.setUsuarioReportanteId(idUsuario);
        if (!esAdministrador(authentication)) {
            dto.setTecnicoAsignadoId(null);
            dto.setPrioridad(null);
        }
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
        Reporte reporte = service.actualizar(id, dto, idUsuario(authentication), esAdministrador(authentication));
        return ResponseEntity.ok(convertirDto(reporte));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private boolean esAdministrador(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(autoridad -> "ROLE_ADMIN".equals(autoridad.getAuthority()));
    }

    private Long idUsuario(Authentication authentication) {
        return Long.valueOf(authentication.getName());
    }

    private void verificarAcceso(Reporte reporte, Authentication authentication) {
        if (!esAdministrador(authentication)
                && !reporte.getUsuarioReportante().getIdUsuario().equals(idUsuario(authentication))) {
            throw new AccessDeniedException("Solo puedes acceder a tus propios reportes");
        }
    }

    private ReporteDTOList convertirDto(Reporte reporte) {
        ReporteDTOList dto = modelMapper.map(reporte, ReporteDTOList.class);
        dto.setUsuarioReportanteId(reporte.getUsuarioReportante().getIdUsuario());
        dto.setTecnicoAsignadoId(reporte.getTecnicoAsignado() == null ? null : reporte.getTecnicoAsignado().getIdUsuario());
        dto.setCategoriaId(reporte.getCategoria().getIdCategoria());
        dto.setCategoriaNombre(reporte.getCategoria().getNombre());
        dto.setUbicacionId(reporte.getUbicacion().getIdUbicacion());
        return dto;
    }

    private List<ReporteDTOList> convertirLista(List<Reporte> reportes) {
        return reportes.stream()
                .map(item -> convertirDto(item))
                .toList();
    }
    @GetMapping("/estadisticas/por-campus")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Contar incidencias por campus y estado", description = "Consulta 4: un JOIN y COUNT. Cuenta incidencias por campus del estado indicado. Ejemplo: estado=ABIERTO. Solo administradores.")
    public ResponseEntity<List<IncidenciasPorCampusDTO>> contarPorCampusYEstado(@RequestParam String estado) {
        return ResponseEntity.ok(service.contarPorCampusYEstado(estado));
    }
}
