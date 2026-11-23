package pe.edu.upc.fixcampus.fixcampus.dtos;

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

    public Long getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(Long idUbicacion) {
        this.idUbicacion = idUbicacion;
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
