package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioDTOList {

    private Long idUsuario;
    private Long rolId;
    private String nombre;
    private String apellido;
    private String correo;
    private String estado;
    private LocalDateTime fechaRegistro;

    public UsuarioDTOList() {
    }

    public UsuarioDTOList(Long idUsuario, Long rolId,
                          String nombre, String apellido,
                          String correo, String estado,
                          LocalDateTime fechaRegistro) {
        this.idUsuario = idUsuario;
        this.rolId = rolId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

}
