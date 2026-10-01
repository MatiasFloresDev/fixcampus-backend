package pe.edu.upc.fixcampus.fixcampus.dtos;

import java.time.LocalDateTime;

public class ComentarioDTOList {

    private Long idComentario;
    private Long reporteId;
    private Long usuarioId;
    private String textoComentario;
    private LocalDateTime fechaComentario;

    public ComentarioDTOList() {
    }

    public ComentarioDTOList(Long idComentario, Long reporteId, Long usuarioId,
                             String textoComentario, LocalDateTime fechaComentario) {
        this.idComentario = idComentario;
        this.reporteId = reporteId;
        this.usuarioId = usuarioId;
        this.textoComentario = textoComentario;
        this.fechaComentario = fechaComentario;
    }

    public Long getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Long idComentario) {
        this.idComentario = idComentario;
    }

    public Long getReporteId() {
        return reporteId;
    }

    public void setReporteId(Long reporteId) {
        this.reporteId = reporteId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
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