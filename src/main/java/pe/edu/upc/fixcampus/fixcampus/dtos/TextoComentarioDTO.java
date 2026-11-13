package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TextoComentarioDTO {
    @NotBlank(message = "El comentario es obligatorio")
    @Size(max = 2000, message = "El comentario admite hasta 2000 caracteres")
    private String textoComentario;

    public String getTextoComentario() {
        return textoComentario;
    }

    public void setTextoComentario(String textoComentario) {
        this.textoComentario = textoComentario;
    }
}
