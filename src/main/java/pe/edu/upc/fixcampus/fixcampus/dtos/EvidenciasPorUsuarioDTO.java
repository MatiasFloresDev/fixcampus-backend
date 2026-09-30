package pe.edu.upc.fixcampus.fixcampus.dtos;

public class EvidenciasPorUsuarioDTO {
    private Long usuarioId;
    private String nombre;
    private String apellido;
    private Long cantidadEvidencias;

    public EvidenciasPorUsuarioDTO(Long usuarioId, String nombre, String apellido,
                                  Long cantidadEvidencias) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cantidadEvidencias = cantidadEvidencias;
    }

    public Long getUsuarioId() { return usuarioId; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public Long getCantidadEvidencias() { return cantidadEvidencias; }
}
