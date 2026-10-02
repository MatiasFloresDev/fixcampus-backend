package pe.edu.upc.fixcampus.fixcampus.dtos;

public class UsuariosPorRolDTO {

    private String nombreRol;
    private Long cantidadUsuarios;

    public UsuariosPorRolDTO() {
    }

    public UsuariosPorRolDTO(String nombreRol, Long cantidadUsuarios) {
        this.nombreRol = nombreRol;
        this.cantidadUsuarios = cantidadUsuarios;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public Long getCantidadUsuarios() {
        return cantidadUsuarios;
    }

    public void setCantidadUsuarios(Long cantidadUsuarios) {
        this.cantidadUsuarios = cantidadUsuarios;
    }
}