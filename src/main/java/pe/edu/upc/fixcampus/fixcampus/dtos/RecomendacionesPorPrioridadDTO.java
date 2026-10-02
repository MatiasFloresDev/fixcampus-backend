package pe.edu.upc.fixcampus.fixcampus.dtos;

public class RecomendacionesPorPrioridadDTO {
    private String prioridad;
    private Long cantidad;

    public RecomendacionesPorPrioridadDTO() { }

    public RecomendacionesPorPrioridadDTO(String prioridad, Long cantidad) {
        this.prioridad = prioridad;
        this.cantidad = cantidad;
    }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public Long getCantidad() { return cantidad; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
}
