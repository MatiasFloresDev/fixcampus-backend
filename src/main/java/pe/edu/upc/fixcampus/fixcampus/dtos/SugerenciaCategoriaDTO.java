package pe.edu.upc.fixcampus.fixcampus.dtos;


public class SugerenciaCategoriaDTO {
    
    private Long categoriaId;
    
    private String nombre;

    public SugerenciaCategoriaDTO() {
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
