package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuariosPorRolDTO {

    private String nombreRol;
    private Long cantidadUsuarios;

    public UsuariosPorRolDTO() {
    }

    public UsuariosPorRolDTO(String nombreRol, Long cantidadUsuarios) {
        this.nombreRol = nombreRol;
        this.cantidadUsuarios = cantidadUsuarios;
    }

}
