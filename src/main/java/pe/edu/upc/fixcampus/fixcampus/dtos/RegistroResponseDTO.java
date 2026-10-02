package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;

@Getter
public class RegistroResponseDTO {

    private Long idUsuario;
    private String correo;
    private String mensaje;

    public RegistroResponseDTO(Long idUsuario, String correo, String mensaje) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.mensaje = mensaje;
    }

}
