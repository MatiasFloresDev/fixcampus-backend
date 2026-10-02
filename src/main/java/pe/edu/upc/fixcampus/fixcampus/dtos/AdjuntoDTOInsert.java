package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class AdjuntoDTOInsert {

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

    public AdjuntoDTOInsert() {
    }

    public AdjuntoDTOInsert(Long reporteId, String nombreArchivo,
                            String urlArchivo, String tipoArchivo) {
        this.reporteId = reporteId;
        this.nombreArchivo = nombreArchivo;
        this.urlArchivo = urlArchivo;
        this.tipoArchivo = tipoArchivo;
    }

}
