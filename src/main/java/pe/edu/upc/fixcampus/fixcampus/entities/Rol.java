package pe.edu.upc.fixcampus.fixcampus.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nivel_acceso", nullable = false, length = 50)
    private String nivelAcceso;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    public Rol() {
    }

    public Rol(Long idRol, String nombre, String nivelAcceso, String descripcion) {
        this.idRol = idRol;
        this.nombre = nombre;
        this.nivelAcceso = nivelAcceso;
        this.descripcion = descripcion;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
