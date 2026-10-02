package pe.edu.upc.fixcampus.fixcampus.entities;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reporte")
@Getter
@Setter
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long idReporte;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario_reportante", nullable = false)
    private Usuario usuarioReportante;

    @ManyToOne
    @JoinColumn(name = "id_tecnico_asignado")
    private Usuario tecnicoAsignado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_ubicacion", nullable = false)
    private Ubicacion ubicacion;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "detalle_ubicacion", length = 255)
    private String detalleUbicacion;

    @Column(name = "prioridad", length = 30)
    private String prioridad;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    public Reporte() {
    }

    public Reporte(Long idReporte, Usuario usuarioReportante,
                   Usuario tecnicoAsignado, Categoria categoria,
                   Ubicacion ubicacion, String titulo, String descripcion,
                   String detalleUbicacion, String prioridad, String estado,
                   LocalDateTime fechaCreacion, LocalDateTime fechaAsignacion,
                   LocalDateTime fechaResolucion) {
        this.idReporte = idReporte;
        this.usuarioReportante = usuarioReportante;
        this.tecnicoAsignado = tecnicoAsignado;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.detalleUbicacion = detalleUbicacion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaResolucion = fechaResolucion;
    }

}
