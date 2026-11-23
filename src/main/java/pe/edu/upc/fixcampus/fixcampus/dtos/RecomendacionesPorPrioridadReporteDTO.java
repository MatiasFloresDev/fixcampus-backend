package pe.edu.upc.fixcampus.fixcampus.dtos;

public class RecomendacionesPorPrioridadReporteDTO {

    private String prioridad;
    private Long cantidad;

    public RecomendacionesPorPrioridadReporteDTO(String prioridad, Long cantidad) {
        this.prioridad = prioridad;
        this.cantidad = cantidad;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public Long getCantidad() {
        return cantidad;
    }
}
