package pe.edu.upc.fixcampus.fixcampus.dtos;

public class LoginResponseDTO {

    private String token;
    private String correo;
    private Long idUsuario;

    public LoginResponseDTO(String token, String correo, Long idUsuario) {
        this.token = token;
        this.correo = correo;
        this.idUsuario = idUsuario;
    }

    public String getToken() {
        return token;
    }

    public String getCorreo() {
        return correo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }
}
