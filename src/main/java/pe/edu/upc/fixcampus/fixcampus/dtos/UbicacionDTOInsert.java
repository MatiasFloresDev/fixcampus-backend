package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UbicacionDTOInsert {

    @NotBlank(message = "El campus es obligatorio")
    @Size(max = 100, message = "El campus no puede superar los 100 caracteres")
    private String campus;

    @NotBlank(message = "El edificio es obligatorio")
    @Size(max = 100, message = "El edificio no puede superar los 100 caracteres")
    private String edificio;

    private Integer piso;

    @Size(max = 150, message = "La zona no puede superar los 150 caracteres")
    private String zona;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 50, message = "El tipo no puede superar los 50 caracteres")
    private String tipo;

    public UbicacionDTOInsert() {
    }

    public UbicacionDTOInsert(String campus, String edificio,
                              Integer piso, String zona, String tipo) {
        this.campus = campus;
        this.edificio = edificio;
        this.piso = piso;
        this.zona = zona;
        this.tipo = tipo;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
