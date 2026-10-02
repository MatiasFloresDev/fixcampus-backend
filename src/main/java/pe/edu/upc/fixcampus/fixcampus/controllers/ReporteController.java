package pe.edu.upc.fixcampus.fixcampus.controllers;

import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;
import java.util.ArrayList;
import org.springframework.security.core.GrantedAuthority;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
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
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IReporteService;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUsuarioRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReporteController {

    private final IReporteService service;
    private final IUsuarioRepository usuarioRepository;

    public ReporteController(IReporteService service, IUsuarioRepository usuarioRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/mis-reportes")
    @Operation(summary = "Listar mis reportes", description = "Muestra solo las incidencias registradas por la cuenta que inició sesión.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<ReporteDTOList> misReportes(Authentication authentication) {
        return convertirLista(service.buscarPorCorreoReportante(authentication.getName()));
    }

    @GetMapping
    @Operation(summary = "Listar reportes por estado", description = "Consulta 1, simple: filtra las incidencias por estado. Sin estado lista todos. Ejemplo: ABIERTO.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<ReporteDTOList>> listar(
            @Parameter(description = "Estado del reporte, por ejemplo ABIERTO") @RequestParam(required = false) String estado) {
        List<Reporte> reportes;
        if (estado != null && !estado.isBlank()) {
            reportes = service.buscarPorEstado(estado);
        } else {
            reportes = service.listar();
        }
        return ResponseEntity.ok(convertirLista(reportes));
    }

    @GetMapping("/estadisticas/por-usuario-mes")
    @Operation(summary = "Contar incidencias por usuario y mes", description = "Consulta 2: JOIN, GROUP BY y COUNT. Cuenta incidencias por usuario, año y mes. Solo administradores.")
    @PreAuthorize("hasRole('ADMIN')")
    public List<IncidenciasPorMesDTO> incidenciasPorUsuarioYMes() {
        return service.contarPorUsuarioYMes();
    }


    @GetMapping("/prioridad/{prioridad}")
    @Operation(summary = "Buscar reportes por prioridad", description = "Consulta 3, simple: lista las incidencias de la prioridad indicada. Ejemplo: ALTA.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<ReporteDTOList>> buscarPorPrioridad(@PathVariable String prioridad) {
        return ResponseEntity.ok(convertirLista(service.buscarPorPrioridad(prioridad)));
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
        Optional<Usuario> usuario =
                usuarioRepository.findByCorreo(authentication.getName());
        if (usuario.isEmpty()) {
            throw new AccessDeniedException("Usuario autenticado no encontrado");
        }
        Long idUsuario = usuario.get().getIdUsuario();
        dto.setUsuarioReportanteId(idUsuario);
        boolean administrador = false;
        for (GrantedAuthority autoridad : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(autoridad.getAuthority())) {
                administrador = true;
                break;
            }
        }
        if (!administrador) {
            dto.setTecnicoAsignadoId(null);
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
        Reporte existente = service.buscarPorId(id);
        boolean administrador = false;
        for (GrantedAuthority autoridad : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(autoridad.getAuthority())) {
                administrador = true;
                break;
            }
        }
        boolean esPropietario = existente.getUsuarioReportante().getCorreo()
                .equalsIgnoreCase(authentication.getName());
        if (!administrador && !esPropietario) {
            throw new AccessDeniedException("Solo puedes editar tus propios reportes");
        }
        // El autor original se conserva también cuando actualiza un administrador.
        dto.setUsuarioReportanteId(existente.getUsuarioReportante().getIdUsuario());
        if (!administrador) {
            dto.setUsuarioReportanteId(existente.getUsuarioReportante().getIdUsuario());
            if (existente.getTecnicoAsignado() == null) {
                dto.setTecnicoAsignadoId(null);
            } else {
                dto.setTecnicoAsignadoId(existente.getTecnicoAsignado().getIdUsuario());
            }
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
        if (reporte.getTecnicoAsignado() == null) {
            dto.setTecnicoAsignadoId(null);
        } else {
            dto.setTecnicoAsignadoId(reporte.getTecnicoAsignado().getIdUsuario());
        }
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

    private List<ReporteDTOList> convertirLista(List<Reporte> reportes) {
        List<ReporteDTOList> lista = new ArrayList<>();
        for (Reporte reporte : reportes) {
            lista.add(convertirDto(reporte));
        }
        return lista;
    }
    @GetMapping("/estadisticas/por-campus")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Contar incidencias por campus y estado", description = "Consulta 4: un JOIN y COUNT. Cuenta incidencias por campus del estado indicado. Ejemplo: estado=ABIERTO. Solo administradores.")
    public List<IncidenciasPorCampusDTO> contarPorCampusYEstado(@RequestParam String estado) {
        return service.contarPorCampusYEstado(estado);
    }
}
