package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecomendacionesPorPrioridadDTO {
    private String prioridad;
    private Long cantidad;

    public RecomendacionesPorPrioridadDTO() { }

    public RecomendacionesPorPrioridadDTO(String prioridad, Long cantidad) {
        this.prioridad = prioridad;
        this.cantidad = cantidad;
    }
}
