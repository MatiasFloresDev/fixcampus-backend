package pe.edu.upc.fixcampus.fixcampus.dtos;

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

    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Long getTotalReportes() { return totalReportes; }
    public void setTotalReportes(Long totalReportes) { this.totalReportes = totalReportes; }
}
