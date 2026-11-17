package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;

public class ReporteDTOInsert {

    private Long usuarioReportanteId;

    @Min(value = 1, message = "El técnico seleccionado no es válido")
    private Long tecnicoAsignadoId;

    @NotNull(message = "Selecciona una categoría")
    @Min(value = 1, message = "La categoría seleccionada no es válida")
    private Long categoriaId;

    @NotNull(message = "Selecciona una ubicación")
    @Min(value = 1, message = "La ubicación seleccionada no es válida")
    private Long ubicacionId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título admite hasta 200 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Size(max = 255, message = "El detalle de ubicación admite hasta 255 caracteres")
    private String detalleUbicacion;

    @Size(max = 30)
    @Pattern(regexp = "MUY_BAJA|BAJA|MEDIA|ALTA|MUY_ALTA", message = "La prioridad debe ser MUY_BAJA, BAJA, MEDIA, ALTA o MUY_ALTA")
    private String prioridad;

    @NotBlank(message = "Selecciona un estado")
    @Pattern(regexp = "ABIERTO|EN_REVISION|ASIGNADO|EN_ATENCION|RESUELTO|CERRADO", message = "Selecciona un estado válido")
    private String estado;

    public Long getUsuarioReportanteId() {
        return usuarioReportanteId;
    }

    public void setUsuarioReportanteId(Long usuarioReportanteId) {
        this.usuarioReportanteId = usuarioReportanteId;
    }

    public Long getTecnicoAsignadoId() {
        return tecnicoAsignadoId;
    }

    public void setTecnicoAsignadoId(Long tecnicoAsignadoId) {
        this.tecnicoAsignadoId = tecnicoAsignadoId;
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

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
