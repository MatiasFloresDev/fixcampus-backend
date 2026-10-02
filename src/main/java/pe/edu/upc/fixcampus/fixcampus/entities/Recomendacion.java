package pe.edu.upc.fixcampus.fixcampus.entities;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recomendacion")
@Getter
@Setter
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recomendacion")
    private Long idRecomendacion;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_reporte", nullable = false, unique = true)
    private Reporte reporte;

    @Column(name = "titulo_sugerido", nullable = false, length = 200)
    private String tituloSugerido;

    @Column(name = "resumen", nullable = false, columnDefinition = "TEXT")
    private String resumen;

    @Column(name = "prioridad_sugerida", nullable = false, length = 30)
    private String prioridadSugerida;

    @Column(name = "justificacion", nullable = false, columnDefinition = "TEXT")
    private String justificacion;

    @Column(name = "fecha_recomendacion", nullable = false)
    private LocalDateTime fechaRecomendacion;

    public Recomendacion() {
    }

    public Recomendacion(Long idRecomendacion, Reporte reporte,
                         String tituloSugerido, String resumen,
                         String prioridadSugerida, String justificacion,
                         LocalDateTime fechaRecomendacion) {
        this.idRecomendacion = idRecomendacion;
        this.reporte = reporte;
        this.tituloSugerido = tituloSugerido;
        this.resumen = resumen;
        this.prioridadSugerida = prioridadSugerida;
        this.justificacion = justificacion;
        this.fechaRecomendacion = fechaRecomendacion;
    }

}
