package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RecomendacionDTOList {

    private Long idRecomendacion;
    private Long reporteId;
    private String tituloSugerido;
    private String resumen;
    private String prioridadSugerida;
    private String justificacion;
    private LocalDateTime fechaRecomendacion;

    public RecomendacionDTOList() {
    }

    public RecomendacionDTOList(Long idRecomendacion, Long reporteId,
                                String tituloSugerido, String resumen,
                                String prioridadSugerida, String justificacion,
                                LocalDateTime fechaRecomendacion) {
        this.idRecomendacion = idRecomendacion;
        this.reporteId = reporteId;
        this.tituloSugerido = tituloSugerido;
        this.resumen = resumen;
        this.prioridadSugerida = prioridadSugerida;
        this.justificacion = justificacion;
        this.fechaRecomendacion = fechaRecomendacion;
    }

}
