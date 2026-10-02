package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidenciasPorCampusDTO {
    private String campus;
    private Long cantidad;

    public IncidenciasPorCampusDTO() { }

    public IncidenciasPorCampusDTO(String campus, Long cantidad) {
        this.campus = campus;
        this.cantidad = cantidad;
    }
}
