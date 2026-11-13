package pe.edu.upc.fixcampus.fixcampus.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recomendacion")
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

    public Long getIdRecomendacion() {
        return idRecomendacion;
    }

    public void setIdRecomendacion(Long idRecomendacion) {
        this.idRecomendacion = idRecomendacion;
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public String getTituloSugerido() {
        return tituloSugerido;
    }

    public void setTituloSugerido(String tituloSugerido) {
        this.tituloSugerido = tituloSugerido;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getPrioridadSugerida() {
        return prioridadSugerida;
    }

    public void setPrioridadSugerida(String prioridadSugerida) {
        this.prioridadSugerida = prioridadSugerida;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public LocalDateTime getFechaRecomendacion() {
        return fechaRecomendacion;
    }

    public void setFechaRecomendacion(LocalDateTime fechaRecomendacion) {
        this.fechaRecomendacion = fechaRecomendacion;
    }
}
