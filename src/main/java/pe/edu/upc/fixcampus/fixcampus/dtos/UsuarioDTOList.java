package pe.edu.upc.fixcampus.fixcampus.dtos;

import java.time.LocalDateTime;

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

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}