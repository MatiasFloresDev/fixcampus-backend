package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public class ComentarioDTO {
    private Long idComentario;
    @NotNull(message = "El id del reporte es obligatorio")
    @Positive(message = "El id del reporte debe ser positivo")
    private Long reporteId;
    @NotNull(message = "El id del usuario es obligatorio")
    @Positive(message = "El id del usuario debe ser positivo")
    private Long usuarioId;
    @NotBlank(message = "El comentario es obligatorio")
    private String textoComentario;
    private LocalDateTime fechaComentario;

    public ComentarioDTO() {
    }

    public ComentarioDTO(Long idComentario, Long reporteId, Long usuarioId,
                         String textoComentario, LocalDateTime fechaComentario) {
        this.idComentario = idComentario;
        this.reporteId = reporteId;
        this.usuarioId = usuarioId;
        this.textoComentario = textoComentario;
        this.fechaComentario = fechaComentario;
    }

    public Long getIdComentario() { return idComentario; }
    public void setIdComentario(Long idComentario) { this.idComentario = idComentario; }
    public Long getReporteId() { return reporteId; }
    public void setReporteId(Long reporteId) { this.reporteId = reporteId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getTextoComentario() { return textoComentario; }
    public void setTextoComentario(String textoComentario) { this.textoComentario = textoComentario; }
    public LocalDateTime getFechaComentario() { return fechaComentario; }
    public void setFechaComentario(LocalDateTime fechaComentario) { this.fechaComentario = fechaComentario; }
}
