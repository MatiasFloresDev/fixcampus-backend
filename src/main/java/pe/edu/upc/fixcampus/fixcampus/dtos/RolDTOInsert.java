package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class RolDTOInsert {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String nombre;

    @NotBlank(message = "El nivel de acceso es obligatorio")
    @Size(max = 50, message = "El nivel de acceso no puede superar los 50 caracteres")
    private String nivelAcceso;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    public RolDTOInsert() {
    }

    public RolDTOInsert(String nombre, String nivelAcceso, String descripcion) {
        this.nombre = nombre;
        this.nivelAcceso = nivelAcceso;
        this.descripcion = descripcion;
    }

}
