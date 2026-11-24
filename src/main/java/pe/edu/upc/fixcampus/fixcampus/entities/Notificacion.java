package pe.edu.upc.fixcampus.fixcampus.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
@Entity
@Table(name = "notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotificacion;
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_reporte", nullable = false)
    private Reporte reporte;
    @Column(nullable = false, length = 30)
    private String tipo;
    @Column(nullable = false, length = 150)
    private String destinatario;
    @Column(nullable = false, length = 20)
    private String estado;
    @Column(length = 300)
    private String mensaje;
    @Column(nullable = false)
    private LocalDateTime fecha;

    public Notificacion() {
    }

    public Long getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Long idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
