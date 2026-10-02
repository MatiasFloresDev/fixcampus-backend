package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolDTOList {

    private Long idRol;
    private String nombre;
    private String nivelAcceso;
    private String descripcion;

    public RolDTOList() {
    }

    public RolDTOList(Long idRol, String nombre,
                      String nivelAcceso, String descripcion) {
        this.idRol = idRol;
        this.nombre = nombre;
        this.nivelAcceso = nivelAcceso;
        this.descripcion = descripcion;
    }

}
