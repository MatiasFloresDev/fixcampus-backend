package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaConReportesDTO {

    private Long idCategoria;
    private String nombre;
    private String descripcion;
    private Long totalReportes;

    public CategoriaConReportesDTO(Long idCategoria, String nombre, String descripcion, Long totalReportes) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.totalReportes = totalReportes;
    }

}
