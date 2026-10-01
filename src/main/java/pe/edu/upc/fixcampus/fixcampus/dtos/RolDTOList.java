package pe.edu.upc.fixcampus.fixcampus.dtos;

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

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}