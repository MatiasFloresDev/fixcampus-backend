package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class UbicacionDTOInsert {

    @NotBlank(message = "El campus es obligatorio")
    @Size(max = 100, message = "El campus no puede superar los 100 caracteres")
    private String campus;

    @NotBlank(message = "El edificio es obligatorio")
    @Size(max = 100, message = "El edificio no puede superar los 100 caracteres")
    private String edificio;

    private Integer piso;

    @Size(max = 150, message = "La zona no puede superar los 150 caracteres")
    private String zona;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 50, message = "El tipo no puede superar los 50 caracteres")
    private String tipo;

    public UbicacionDTOInsert() {
    }

    public UbicacionDTOInsert(String campus, String edificio,
                              Integer piso, String zona, String tipo) {
        this.campus = campus;
        this.edificio = edificio;
        this.piso = piso;
        this.zona = zona;
        this.tipo = tipo;
    }

}
