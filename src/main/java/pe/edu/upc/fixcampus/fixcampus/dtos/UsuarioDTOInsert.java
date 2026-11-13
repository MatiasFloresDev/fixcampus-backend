package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioDTOInsert {
    @NotNull
    private Long rolId;
    @NotBlank
    @Size(max = 100)
    private String nombre;
    @NotBlank
    @Size(max = 100)
    private String apellido;
    @NotBlank
    @Email
    @Size(max = 150)
    private String correo;
    // Obligatoria al crear; opcional al actualizar para conservar la contraseña actual.
    private String password;
    @NotBlank
    @Size(max = 30)
    private String estado;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
