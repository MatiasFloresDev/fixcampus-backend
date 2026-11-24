package pe.edu.upc.fixcampus.fixcampus.dtos;

public class IncidenciasPorCategoriaDTO {
    private String categoria;
    private Long cantidad;

    public IncidenciasPorCategoriaDTO() {
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
