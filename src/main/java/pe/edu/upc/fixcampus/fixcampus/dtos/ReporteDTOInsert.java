package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ReporteDTOInsert {

    @NotNull(message = "La categoría es obligatoria")
    @Positive(message = "La categoría debe ser válida")
    private Long categoriaId;

    @NotNull(message = "La ubicación es obligatoria")
    @Positive(message = "La ubicación debe ser válida")
    private Long ubicacionId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Size(max = 255, message = "El detalle de ubicación no puede superar los 255 caracteres")
    private String detalleUbicacion;

    public ReporteDTOInsert() {
    }

    public ReporteDTOInsert(Long categoriaId, Long ubicacionId,
                            String titulo, String descripcion,
                            String detalleUbicacion) {
        this.categoriaId = categoriaId;
        this.ubicacionId = ubicacionId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.detalleUbicacion = detalleUbicacion;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getUbicacionId() {
        return ubicacionId;
    }

    public void setUbicacionId(Long ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDetalleUbicacion() {
        return detalleUbicacion;
    }

    public void setDetalleUbicacion(String detalleUbicacion) {
        this.detalleUbicacion = detalleUbicacion;
    }
}
