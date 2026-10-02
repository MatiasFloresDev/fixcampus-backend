package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;

@Getter
public class IncidenciasPorMesDTO {
    private Long usuarioId;
    private String nombre;
    private String apellido;
    private Integer anio;
    private Integer mes;
    private Long cantidad;

    public IncidenciasPorMesDTO(Long usuarioId, String nombre, String apellido,
                                Integer anio, Integer mes, Long cantidad) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.anio = anio;
        this.mes = mes;
        this.cantidad = cantidad;
    }

}
