package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;

import java.util.List;

public interface IReporteService {

    List<Reporte> listar();
    Reporte buscarPorId(Long id);
    Reporte registrar(ReporteDTOInsert dto);
    Reporte actualizar(Long id, ReporteDTOInsert dto);
    void eliminar(Long id);
    List<Reporte> buscarPorEstado(String estado);
    List<Reporte> buscarPorCorreoReportante(String correo);
    List<IncidenciasPorMesDTO> contarPorUsuarioYMes();
    List<Reporte> buscarPorPrioridad(String prioridad);
    List<IncidenciasPorCampusDTO> contarPorCampusYEstado(String estado);
}
