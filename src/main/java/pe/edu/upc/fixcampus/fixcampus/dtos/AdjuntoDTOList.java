package pe.edu.upc.fixcampus.fixcampus.dtos;

import java.time.LocalDateTime;

public class AdjuntoDTOList {

    private Long idAdjunto;
    private Long reporteId;
    private String nombreArchivo;
    private String urlArchivo;
    private String tipoArchivo;
    private LocalDateTime fechaSubida;

    public AdjuntoDTOList() {
    }

    public AdjuntoDTOList(Long idAdjunto, Long reporteId,
                          String nombreArchivo, String urlArchivo,
                          String tipoArchivo, LocalDateTime fechaSubida) {
        this.idAdjunto = idAdjunto;
        this.reporteId = reporteId;
        this.nombreArchivo = nombreArchivo;
        this.urlArchivo = urlArchivo;
        this.tipoArchivo = tipoArchivo;
        this.fechaSubida = fechaSubida;
    }

    public Long getIdAdjunto() {
        return idAdjunto;
    }

    public void setIdAdjunto(Long idAdjunto) {
        this.idAdjunto = idAdjunto;
    }

    public Long getReporteId() {
        return reporteId;
    }

    public void setReporteId(Long reporteId) {
        this.reporteId = reporteId;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
}
