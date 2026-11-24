package pe.edu.upc.fixcampus.fixcampus.dtos;

public class UsuariosPorRolDTO {
    private String rol;
    private Long cantidad;

    public UsuariosPorRolDTO() {
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
