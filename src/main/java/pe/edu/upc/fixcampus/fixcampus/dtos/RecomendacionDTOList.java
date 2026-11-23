package pe.edu.upc.fixcampus.fixcampus.dtos;

import java.time.LocalDateTime;

public class RecomendacionDTOList {

    private Long idRecomendacion;
    private Long reporteId;
    private String tituloSugerido;
    private String resumen;
    private String prioridadSugerida;
    private String justificacion;
    private LocalDateTime fechaRecomendacion;

    public RecomendacionDTOList() {
    }

    public RecomendacionDTOList(Long idRecomendacion, Long reporteId,
                                String tituloSugerido, String resumen,
                                String prioridadSugerida, String justificacion,
                                LocalDateTime fechaRecomendacion) {
        this.idRecomendacion = idRecomendacion;
        this.reporteId = reporteId;
        this.tituloSugerido = tituloSugerido;
        this.resumen = resumen;
        this.prioridadSugerida = prioridadSugerida;
        this.justificacion = justificacion;
        this.fechaRecomendacion = fechaRecomendacion;
    }

    public Long getIdRecomendacion() {
        return idRecomendacion;
    }

    public void setIdRecomendacion(Long idRecomendacion) {
        this.idRecomendacion = idRecomendacion;
    }

    public Long getReporteId() {
        return reporteId;
    }

    public void setReporteId(Long reporteId) {
        this.reporteId = reporteId;
    }

    public String getTituloSugerido() {
        return tituloSugerido;
    }

    public void setTituloSugerido(String tituloSugerido) {
        this.tituloSugerido = tituloSugerido;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getPrioridadSugerida() {
        return prioridadSugerida;
    }

    public void setPrioridadSugerida(String prioridadSugerida) {
        this.prioridadSugerida = prioridadSugerida;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public LocalDateTime getFechaRecomendacion() {
        return fechaRecomendacion;
    }

    public void setFechaRecomendacion(LocalDateTime fechaRecomendacion) {
        this.fechaRecomendacion = fechaRecomendacion;
    }
}
