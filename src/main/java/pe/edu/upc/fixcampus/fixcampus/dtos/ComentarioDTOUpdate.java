
package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;

public class ComentarioDTOUpdate {

    @NotBlank(message = "El comentario es obligatorio")
    private String textoComentario;

    public ComentarioDTOUpdate() {
    }

    public ComentarioDTOUpdate(String textoComentario) {
        this.textoComentario = textoComentario;
    }

    public String getTextoComentario() {
        return textoComentario;
    }

    public void setTextoComentario(String textoComentario) {
        this.textoComentario = textoComentario;
    }
}
