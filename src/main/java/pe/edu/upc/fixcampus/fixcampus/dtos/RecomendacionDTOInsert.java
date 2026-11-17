package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class RecomendacionDTOInsert {

    @NotNull(message = "El reporte es obligatorio")
    @Min(value = 1, message = "El reporte seleccionado no es válido")
    private Long reporteId;

    @NotBlank(message = "El título sugerido es obligatorio")
    @Size(max = 200, message = "El título sugerido no puede superar los 200 caracteres")
    private String tituloSugerido;

    @NotBlank(message = "El resumen es obligatorio")
    private String resumen;

    @NotBlank(message = "La prioridad sugerida es obligatoria")
    @Size(max = 30, message = "La prioridad sugerida no puede superar los 30 caracteres")
    @Pattern(regexp = "MUY_BAJA|BAJA|MEDIA|ALTA|MUY_ALTA", message = "Selecciona una prioridad válida")
    private String prioridadSugerida;

    @NotBlank(message = "La justificación es obligatoria")
    private String justificacion;

    public RecomendacionDTOInsert() {

    }

    public RecomendacionDTOInsert(Long reporteId, String tituloSugerido,
                                  String resumen, String prioridadSugerida,
                                  String justificacion) {
        this.reporteId = reporteId;
        this.tituloSugerido = tituloSugerido;
        this.resumen = resumen;
        this.prioridadSugerida = prioridadSugerida;
        this.justificacion = justificacion;
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
}
