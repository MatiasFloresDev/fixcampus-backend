package pe.edu.upc.fixcampus.fixcampus.dtos;

public class IncidenciasPorCategoriaDTO {
    private String categoria;
    private Long cantidad;

    public IncidenciasPorCategoriaDTO(String categoria, Long cantidad) {
        this.categoria = categoria;
        this.cantidad = cantidad;
    }

    public String getCategoria() { return categoria; }
    public Long getCantidad() { return cantidad; }
}
