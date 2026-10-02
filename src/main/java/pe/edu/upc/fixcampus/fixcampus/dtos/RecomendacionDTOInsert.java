package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class RecomendacionDTOInsert {

    @NotNull(message = "El reporte es obligatorio")
    private Long reporteId;

    @NotBlank(message = "El título sugerido es obligatorio")
    @Size(max = 200, message = "El título sugerido no puede superar los 200 caracteres")
    private String tituloSugerido;

    @NotBlank(message = "El resumen es obligatorio")
    private String resumen;

    @NotBlank(message = "La prioridad sugerida es obligatoria")
    @Size(max = 30, message = "La prioridad sugerida no puede superar los 30 caracteres")
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

}
