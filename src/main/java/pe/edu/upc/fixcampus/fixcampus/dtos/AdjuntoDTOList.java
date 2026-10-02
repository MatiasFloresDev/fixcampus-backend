package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdjuntoDTOList {

    private Long idAdjunto;
    private Long reporteId;
    private String nombreArchivo;
    private String urlArchivo;
    private String tipoArchivo;
    private LocalDateTime fechaSubida;

    public AdjuntoDTOList() {
    }

    public AdjuntoDTOList(Long idAdjunto, Long reporteId,
                          String nombreArchivo, String urlArchivo,
                          String tipoArchivo, LocalDateTime fechaSubida) {
        this.idAdjunto = idAdjunto;
        this.reporteId = reporteId;
        this.nombreArchivo = nombreArchivo;
        this.urlArchivo = urlArchivo;
        this.tipoArchivo = tipoArchivo;
        this.fechaSubida = fechaSubida;
    }

}
