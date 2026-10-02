package pe.edu.upc.fixcampus.fixcampus.entities;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "adjunto")
@Getter
@Setter
public class Adjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adjunto")
    private Long idAdjunto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_reporte", nullable = false)
    private Reporte reporte;

    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;

    @Column(name = "url_archivo", nullable = false, length = 500)
    private String urlArchivo;

    @Column(name = "tipo_archivo", length = 100)
    private String tipoArchivo;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;

    public Adjunto() {
    }

    public Adjunto(Long idAdjunto, Reporte reporte, String nombreArchivo,
                   String urlArchivo, String tipoArchivo, LocalDateTime fechaSubida) {
        this.idAdjunto = idAdjunto;
        this.reporte = reporte;
        this.nombreArchivo = nombreArchivo;
        this.urlArchivo = urlArchivo;
        this.tipoArchivo = tipoArchivo;
        this.fechaSubida = fechaSubida;
    }

}
