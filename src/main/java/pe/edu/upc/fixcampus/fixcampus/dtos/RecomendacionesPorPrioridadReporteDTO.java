package pe.edu.upc.fixcampus.fixcampus.dtos;

public class RecomendacionesPorPrioridadReporteDTO {
    private String prioridad;
    private Long cantidad;

    public RecomendacionesPorPrioridadReporteDTO() {
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
