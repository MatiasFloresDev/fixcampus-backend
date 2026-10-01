package pe.edu.upc.fixcampus.fixcampus.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCategoriaDTO;
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
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;
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
    @GetMapping("/estadisticas/por-categoria")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Contar incidencias por categoría entre fechas", description = "Consulta 18: JOIN y COUNT. Incluye desde y excluye hasta. Ejemplo: desde=2026-09-01, hasta=2026-10-01.")
    public List<IncidenciasPorCategoriaDTO> incidenciasPorCategoria(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return service.contarPorCategoriaEntreFechas(desde, hasta);
    }

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

        return ResponseEntity.ok(convertirLista(reportes));
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

    @GetMapping("/prioridad/{prioridad}")
    @Operation(summary = "Buscar reportes por prioridad", description = "Lista las incidencias que tienen la prioridad indicada.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<ReporteDTOList>> buscarPorPrioridad(@PathVariable String prioridad) {
        return ResponseEntity.ok(convertirLista(service.buscarPorPrioridad(prioridad)));
    }

    @GetMapping("/campus/{campus}")
    @Operation(summary = "Buscar reportes por campus", description = "Lista las incidencias cuya ubicación pertenece al campus indicado. Usa una consulta JOIN.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<ReporteDTOList>> buscarPorCampus(@PathVariable String campus) {
        return ResponseEntity.ok(convertirLista(service.buscarPorCampus(campus)));
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

    private List<ReporteDTOList> convertirLista(List<Reporte> reportes) {
        List<ReporteDTOList> lista = new ArrayList<>();
        for (Reporte reporte : reportes) {
            lista.add(convertirDto(reporte));
        }
        return lista;
    }
}
