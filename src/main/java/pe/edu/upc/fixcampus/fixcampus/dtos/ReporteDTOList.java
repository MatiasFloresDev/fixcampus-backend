package pe.edu.upc.fixcampus.fixcampus.dtos;

import java.time.LocalDateTime;

public class ReporteDTOList {

    private Long idReporte;
    private Long usuarioReportanteId;
    private Long tecnicoAsignadoId;
    private Long categoriaId;
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

    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public Long getUsuarioReportanteId() {
        return usuarioReportanteId;
    }

    public void setUsuarioReportanteId(Long usuarioReportanteId) {
        this.usuarioReportanteId = usuarioReportanteId;
    }

    public Long getTecnicoAsignadoId() {
        return tecnicoAsignadoId;
    }

    public void setTecnicoAsignadoId(Long tecnicoAsignadoId) {
        this.tecnicoAsignadoId = tecnicoAsignadoId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getUbicacionId() {
        return ubicacionId;
    }

    public void setUbicacionId(Long ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDetalleUbicacion() {
        return detalleUbicacion;
    }

    public void setDetalleUbicacion(String detalleUbicacion) {
        this.detalleUbicacion = detalleUbicacion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }
}