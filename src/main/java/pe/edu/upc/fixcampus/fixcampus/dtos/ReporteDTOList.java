package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReporteDTOList {

    private Long idReporte;
    private Long usuarioReportanteId;
    private Long tecnicoAsignadoId;
    private Long categoriaId;
    private String categoriaNombre;
    private Long ubicacionId;
    private String titulo;
    private String descripcion;
    private String detalleUbicacion;
    private String prioridad;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaResolucion;

    public ReporteDTOList() {
    }

    public ReporteDTOList(Long idReporte, Long usuarioReportanteId,
                          Long tecnicoAsignadoId, Long categoriaId,
                          Long ubicacionId, String titulo, String descripcion,
                          String detalleUbicacion, String prioridad, String estado,
                          LocalDateTime fechaCreacion, LocalDateTime fechaAsignacion,
                          LocalDateTime fechaResolucion) {
        this.idReporte = idReporte;
        this.usuarioReportanteId = usuarioReportanteId;
        this.tecnicoAsignadoId = tecnicoAsignadoId;
        this.categoriaId = categoriaId;
        this.ubicacionId = ubicacionId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.detalleUbicacion = detalleUbicacion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaResolucion = fechaResolucion;
    }

}
