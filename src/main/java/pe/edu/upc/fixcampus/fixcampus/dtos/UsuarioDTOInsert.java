package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class UsuarioDTOInsert {
    @NotNull private Long rolId;
    @NotBlank @Size(max = 100) private String nombre;
    @NotBlank @Size(max = 100) private String apellido;
    @NotBlank @Email @Size(max = 150) private String correo;
    // Obligatoria al crear; opcional al actualizar para conservar la contraseña actual.
    private String password;
    @NotBlank @Size(max = 30) private String estado;

}
