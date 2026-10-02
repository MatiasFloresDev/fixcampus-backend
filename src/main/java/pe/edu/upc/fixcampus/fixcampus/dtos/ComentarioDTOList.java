package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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

}
