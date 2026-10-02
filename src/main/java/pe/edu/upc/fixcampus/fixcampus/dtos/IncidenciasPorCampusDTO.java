package pe.edu.upc.fixcampus.fixcampus.dtos;

public class IncidenciasPorCampusDTO {
    private String campus;
    private Long cantidad;

    public IncidenciasPorCampusDTO() { }

    public IncidenciasPorCampusDTO(String campus, Long cantidad) {
        this.campus = campus;
        this.cantidad = cantidad;
    }
    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }
    public Long getCantidad() { return cantidad; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
}
