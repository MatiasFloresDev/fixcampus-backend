package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComentariosPorReporteDTO {
    private Long reporteId;
    private String titulo;
    private Long cantidad;

    public ComentariosPorReporteDTO() { }

    public ComentariosPorReporteDTO(Long reporteId, String titulo, Long cantidad) {
        this.reporteId = reporteId;
        this.titulo = titulo;
        this.cantidad = cantidad;
    }
}
