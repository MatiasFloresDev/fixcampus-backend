package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class ComentarioDTOInsert {

    @NotNull(message = "El reporte es obligatorio")
    @Min(value = 1, message = "El reporte seleccionado no es válido")
    private Long reporteId;

    @NotNull(message = "El usuario es obligatorio")
    @Min(value = 1, message = "El usuario seleccionado no es válido")
    private Long usuarioId;

    @NotBlank(message = "El comentario es obligatorio")
    @Size(max = 2000, message = "El comentario admite hasta 2000 caracteres")
    private String textoComentario;

    public ComentarioDTOInsert() {

    }

    public ComentarioDTOInsert(Long reporteId, String textoComentario) {
        this.reporteId = reporteId;
        this.textoComentario = textoComentario;
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
}
