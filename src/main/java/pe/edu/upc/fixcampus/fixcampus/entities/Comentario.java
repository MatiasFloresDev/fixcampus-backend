package pe.edu.upc.fixcampus.fixcampus.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long idComentario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_reporte", nullable = false)
    private Reporte reporte;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "texto_comentario", nullable = false, columnDefinition = "TEXT")
    private String textoComentario;

    @Column(name = "fecha_comentario", nullable = false)
    private LocalDateTime fechaComentario;

    public Comentario() {
    }

    public Comentario(Long idComentario, Reporte reporte, Usuario usuario,
                      String textoComentario, LocalDateTime fechaComentario) {
        this.idComentario = idComentario;
        this.reporte = reporte;
        this.usuario = usuario;
        this.textoComentario = textoComentario;
        this.fechaComentario = fechaComentario;
    }

    public Long getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Long idComentario) {
        this.idComentario = idComentario;
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTextoComentario() {
        return textoComentario;
    }

    public void setTextoComentario(String textoComentario) {
        this.textoComentario = textoComentario;
    }

    public LocalDateTime getFechaComentario() {
        return fechaComentario;
    }

    public void setFechaComentario(LocalDateTime fechaComentario) {
        this.fechaComentario = fechaComentario;
    }
}
