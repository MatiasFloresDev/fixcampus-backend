package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ComentarioDTOInsert {

    @NotNull(message = "El reporte es obligatorio")
    private Long reporteId;

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El comentario es obligatorio")
    private String textoComentario;

    public ComentarioDTOInsert() {
    }

    public ComentarioDTOInsert(Long reporteId, String textoComentario) {
        this.reporteId = reporteId;
        this.textoComentario = textoComentario;
    }

}
