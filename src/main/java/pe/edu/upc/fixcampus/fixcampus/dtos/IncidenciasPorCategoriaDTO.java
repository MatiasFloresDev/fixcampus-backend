package pe.edu.upc.fixcampus.fixcampus.dtos;

public class IncidenciasPorCategoriaDTO {
    private Long categoriaId;
    private String categoria;
    private Long cantidad;

    public IncidenciasPorCategoriaDTO(Long categoriaId, String categoria, Long cantidad) {
        this.categoriaId = categoriaId;
        this.categoria = categoria;
        this.cantidad = cantidad;
    }

    public Long getCategoriaId() { return categoriaId; }
    public String getCategoria() { return categoria; }
    public Long getCantidad() { return cantidad; }
}
