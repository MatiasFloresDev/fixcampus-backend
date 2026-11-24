package pe.edu.upc.fixcampus.fixcampus.dtos;

public class UsuariosPorRolDTO {

    private String rol;
    private Long cantidad;

    public UsuariosPorRolDTO(String rol, Long cantidad) {
        this.rol = rol;
        this.cantidad = cantidad;
    }

    public String getRol() {
        return rol;
    }

    public Long getCantidad() {
        return cantidad;
    }
}