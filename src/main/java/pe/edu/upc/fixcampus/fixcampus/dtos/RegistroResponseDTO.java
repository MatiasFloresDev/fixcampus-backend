package pe.edu.upc.fixcampus.fixcampus.dtos;

public class RegistroResponseDTO {

    private Long idUsuario;
    private String correo;
    private String mensaje;

    public RegistroResponseDTO(Long idUsuario, String correo, String mensaje) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.mensaje = mensaje;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public String getMensaje() {
        return mensaje;
    }
}
