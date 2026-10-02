package pe.edu.upc.fixcampus.fixcampus.entities;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "rol")
@Getter
@Setter
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

}
