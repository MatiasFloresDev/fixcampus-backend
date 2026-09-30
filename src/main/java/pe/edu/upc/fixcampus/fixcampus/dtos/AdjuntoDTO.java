package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class AdjuntoDTO {
    private Long idAdjunto;
    @NotNull(message = "El id del reporte es obligatorio")
    @Positive(message = "El id del reporte debe ser positivo")
    private Long reporteId;
    @NotBlank(message = "El nombre del archivo es obligatorio")
    @Size(max = 255, message = "El nombre del archivo no puede superar los 255 caracteres")
    private String nombreArchivo;
    @NotBlank(message = "La URL del archivo es obligatoria")
    @Size(max = 500, message = "La URL del archivo no puede superar los 500 caracteres")
    private String urlArchivo;
    @Size(max = 100, message = "El tipo de archivo no puede superar los 100 caracteres")
    private String tipoArchivo;
    private LocalDateTime fechaSubida;

    public AdjuntoDTO() {
    }

    public AdjuntoDTO(Long idAdjunto, Long reporteId, String nombreArchivo,
                      String urlArchivo, String tipoArchivo, LocalDateTime fechaSubida) {
        this.idAdjunto = idAdjunto;
        this.reporteId = reporteId;
        this.nombreArchivo = nombreArchivo;
        this.urlArchivo = urlArchivo;
        this.tipoArchivo = tipoArchivo;
        this.fechaSubida = fechaSubida;
    }

    public Long getIdAdjunto() { return idAdjunto; }
    public void setIdAdjunto(Long idAdjunto) { this.idAdjunto = idAdjunto; }
    public Long getReporteId() { return reporteId; }
    public void setReporteId(Long reporteId) { this.reporteId = reporteId; }
    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }
    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }
    public String getTipoArchivo() { return tipoArchivo; }
    public void setTipoArchivo(String tipoArchivo) { this.tipoArchivo = tipoArchivo; }
    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }
}
