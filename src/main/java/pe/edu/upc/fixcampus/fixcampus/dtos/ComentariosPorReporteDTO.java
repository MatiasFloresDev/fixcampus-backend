package pe.edu.upc.fixcampus.fixcampus.dtos;

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
    public Long getReporteId() { return reporteId; }
    public void setReporteId(Long reporteId) { this.reporteId = reporteId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Long getCantidad() { return cantidad; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
}
