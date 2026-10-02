package pe.edu.upc.fixcampus.fixcampus.entities;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "ubicacion")
@Getter
@Setter
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Long idUbicacion;

    @Column(name = "campus", nullable = false, length = 100)
    private String campus;

    @Column(name = "edificio", nullable = false, length = 100)
    private String edificio;

    @Column(name = "piso")
    private Integer piso;

    @Column(name = "zona", length = 150)
    private String zona;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    public Ubicacion() {
    }

    public Ubicacion(Long idUbicacion, String campus, String edificio,
                     Integer piso, String zona, String tipo) {
        this.idUbicacion = idUbicacion;
        this.campus = campus;
        this.edificio = edificio;
        this.piso = piso;
        this.zona = zona;
        this.tipo = tipo;
    }

}
