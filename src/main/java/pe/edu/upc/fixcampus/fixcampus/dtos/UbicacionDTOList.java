package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UbicacionDTOList {

    private Long idUbicacion;
    private String campus;
    private String edificio;
    private Integer piso;
    private String zona;
    private String tipo;

    public UbicacionDTOList() {
    }

    public UbicacionDTOList(Long idUbicacion, String campus,
                            String edificio, Integer piso,
                            String zona, String tipo) {
        this.idUbicacion = idUbicacion;
        this.campus = campus;
        this.edificio = edificio;
        this.piso = piso;
        this.zona = zona;
        this.tipo = tipo;
    }

}
