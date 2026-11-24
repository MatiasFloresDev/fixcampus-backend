package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCategoriaDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOList;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOUpdate;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.ICategoriaService;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IReporteService;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUbicacionService;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReporteController {

    private ReporteDTOList convertir(Reporte reporte) {
        ReporteDTOList dto = modelMapper.map(reporte, ReporteDTOList.class);
        dto.setUsuarioReportanteNombre(reporte.getUsuarioReportante().getNombre() + " " + reporte.getUsuarioReportante().getApellido());
        if (reporte.getTecnicoAsignado() != null) dto.setTecnicoAsignadoNombre(reporte.getTecnicoAsignado().getNombre() + " " + reporte.getTecnicoAsignado().getApellido());
        return dto;
    }
    private boolean esAdmin(Authentication auth) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private void verificarAcceso(Reporte reporte, Authentication auth) {
        if (!esAdmin(auth) && !reporte.getUsuarioReportante().getCorreo().equalsIgnoreCase(auth.getName())
            && (reporte.getTecnicoAsignado() == null || !reporte.getTecnicoAsignado().getCorreo().equalsIgnoreCase(auth.getName()))) {
            throw new org.springframework.security.access.AccessDeniedException("No tienes permiso para este reporte");
        }
    }

    private final IReporteService reporteService;
    private final IUsuarioService usuarioService;
    private final ICategoriaService categoriaService;
    private final IUbicacionService ubicacionService;
    private final ModelMapper modelMapper;

    public ReporteController(
            IReporteService reporteService,
            IUsuarioService usuarioService,
            ICategoriaService categoriaService,
            IUbicacionService ubicacionService,
            ModelMapper modelMapper) {

        this.reporteService = reporteService;
        this.usuarioService = usuarioService;
        this.categoriaService = categoriaService;
        this.ubicacionService = ubicacionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/mis-reportes")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<ReporteDTOList>> misReportes(
            Authentication authentication) {

        List<ReporteDTOList> lista =
                reporteService
                        .buscarPorCorreoReportante(
                                authentication.getName()
                        )
                        .stream()
                        .map(reporte ->
                                convertir(reporte)
                        )
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'TECNICO')")
    public ResponseEntity<List<ReporteDTOList>> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String correo, Authentication authentication) {

        List<Reporte> reportes;

        if (estado != null && !estado.isBlank()) {
            reportes =
                    reporteService.buscarPorEstado(estado);

        } else if (categoria != null
                && !categoria.isBlank()) {

            reportes =
                    reporteService.buscarPorCategoria(categoria);

        } else if (correo != null
                && !correo.isBlank()) {

            reportes =
                    reporteService
                            .buscarPorCorreoReportante(correo);

        } else {
            reportes =
                    reporteService.listar();
        }

        if (!esAdmin(authentication)) reportes = reportes.stream().filter(r -> r.getUsuarioReportante().getCorreo().equalsIgnoreCase(authentication.getName())).toList();

        List<ReporteDTOList> lista = reportes
                .stream()
                .map(reporte ->
                        modelMapper.map(
                                reporte,
                                ReporteDTOList.class
                        )
                )
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'TECNICO')")
    public ResponseEntity<ReporteDTOList> buscarPorId(
            @PathVariable Long id, Authentication authentication) {

        Reporte reporte = reporteService.buscarPorId(id);
        verificarAcceso(reporte, authentication);

        ReporteDTOList dto =
                convertir(reporte);

        dto.setUsuarioReportanteNombre(reporte.getUsuarioReportante().getNombre() + " " + reporte.getUsuarioReportante().getApellido());
        if (reporte.getTecnicoAsignado() != null) dto.setTecnicoAsignadoNombre(reporte.getTecnicoAsignado().getNombre() + " " + reporte.getTecnicoAsignado().getApellido());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ReporteDTOList> registrar(
            @Valid @RequestBody ReporteDTOInsert dto,
            Authentication authentication) {

        Usuario usuarioReportante =
                usuarioService.buscarPorCorreo(
                        authentication.getName()
                );

        Categoria categoria =
                categoriaService.buscarPorId(
                        dto.getCategoriaId()
                );

        Ubicacion ubicacion =
                ubicacionService.buscarPorId(
                        dto.getUbicacionId()
                );

        Reporte reporte =
                modelMapper.map(
                        dto,
                        Reporte.class
                );

        reporte.setUsuarioReportante(
                usuarioReportante
        );

        reporte.setCategoria(
                categoria
        );

        reporte.setUbicacion(
                ubicacion
        );

        Reporte guardado =
                reporteService.registrar(reporte);

        ReporteDTOList responseDTO =
                modelMapper.map(
                        guardado,
                        ReporteDTOList.class
                );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(
                        guardado.getIdReporte()
                )
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ReporteDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReporteDTOUpdate dto, Authentication authentication) {

        Reporte anterior = reporteService.buscarPorId(id);
        verificarAcceso(anterior, authentication);
        if (!esAdmin(authentication)) {
            dto.setEstado(anterior.getEstado());
            dto.setPrioridad(anterior.getPrioridad());
            dto.setTecnicoAsignadoId(anterior.getTecnicoAsignado() == null ? null : anterior.getTecnicoAsignado().getIdUsuario());
        }

        Categoria categoria =
                categoriaService.buscarPorId(
                        dto.getCategoriaId()
                );

        Ubicacion ubicacion =
                ubicacionService.buscarPorId(
                        dto.getUbicacionId()
                );

        Usuario tecnicoAsignado = null;

        if (dto.getTecnicoAsignadoId() != null) {
            tecnicoAsignado =
                    usuarioService.buscarPorId(
                            dto.getTecnicoAsignadoId()
                    );

            if (tecnicoAsignado.getRol() == null ||
                    !"TECNICO".equalsIgnoreCase(tecnicoAsignado.getRol().getNombre().replace("ROLE_", ""))) {
                throw new IllegalArgumentException(
                        "El usuario asignado debe tener el rol TECNICO"
                );
            }
        }

        Reporte reporte =
                modelMapper.map(
                        dto,
                        Reporte.class
                );

        reporte.setCategoria(
                categoria
        );

        reporte.setUbicacion(
                ubicacion
        );

        reporte.setTecnicoAsignado(
                tecnicoAsignado
        );

        Reporte actualizado =
                reporteService.actualizar(
                        id,
                        reporte
                );

        ReporteDTOList responseDTO =
                modelMapper.map(
                        actualizado,
                        ReporteDTOList.class
                );

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        reporteService.eliminar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/prioridad/{prioridad}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReporteDTOList>>
    buscarPorPrioridad(
            @PathVariable String prioridad) {

        List<ReporteDTOList> lista =
                reporteService
                        .buscarPorPrioridad(prioridad)
                        .stream()
                        .map(reporte ->
                                convertir(reporte)
                        )
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/estadisticas/por-usuario-mes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<IncidenciasPorMesDTO>>
    incidenciasPorUsuarioYMes() {

        List<IncidenciasPorMesDTO> lista =
                reporteService.contarPorUsuarioYMes()
                        .stream()
                        .map(fila -> new IncidenciasPorMesDTO(
                                ((Number) fila[0]).longValue(),
                                (String) fila[1],
                                (String) fila[2],
                                ((Number) fila[3]).intValue(),
                                ((Number) fila[4]).intValue(),
                                ((Number) fila[5]).longValue()
                        ))
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/estadisticas/por-categoria")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<IncidenciasPorCategoriaDTO>>
    incidenciasPorCategoria() {

        List<IncidenciasPorCategoriaDTO> lista =
                reporteService.contarPorCategoria()
                        .stream()
                        .map(fila -> new IncidenciasPorCategoriaDTO(
                                (String) fila[0],
                                ((Number) fila[1]).longValue()
                        ))
                        .toList();

        return ResponseEntity.ok(lista);
    }
}
