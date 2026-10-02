package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class ReporteDTOInsert {

    private Long usuarioReportanteId;

    private Long tecnicoAsignadoId;

    @NotNull
    private Long categoriaId;

    @NotNull
    private Long ubicacionId;

    @NotBlank
    @Size(max = 200)
    private String titulo;

    @NotBlank
    private String descripcion;

    @Size(max = 255)
    private String detalleUbicacion;

    @Size(max = 30)
    @Pattern(regexp = "MUY_BAJA|BAJA|MEDIA|ALTA|MUY_ALTA", message = "La prioridad debe ser MUY_BAJA, BAJA, MEDIA, ALTA o MUY_ALTA")
    private String prioridad;

    @NotBlank
    @Size(max = 30)
    private String estado;

}
