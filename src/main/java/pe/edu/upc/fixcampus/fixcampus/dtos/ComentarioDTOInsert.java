package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ComentarioDTOInsert {

    @NotNull(message = "El reporte es obligatorio")
    private Long reporteId;

    @NotBlank(message = "El comentario es obligatorio")
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

    public String getTextoComentario() {
        return textoComentario;
    }

    public void setTextoComentario(String textoComentario) {
        this.textoComentario = textoComentario;
    }
}