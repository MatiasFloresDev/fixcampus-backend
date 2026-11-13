package pe.edu.upc.fixcampus.fixcampus.dtos;

public class SugerenciaCategoriaDTO {

    private Long categoriaId;

    public SugerenciaCategoriaDTO() {

    }

    public SugerenciaCategoriaDTO(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }
}
