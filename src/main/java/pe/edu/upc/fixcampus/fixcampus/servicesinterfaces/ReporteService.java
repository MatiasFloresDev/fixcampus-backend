package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;

import java.util.List;

public interface ReporteService {

    List<Reporte> listar();
    Reporte buscarPorId(Long id);
    Reporte registrar(ReporteDTOInsert dto);
    Reporte actualizar(Long id, ReporteDTOInsert dto);
    void eliminar(Long id);
    List<Reporte> buscarPorEstado(String estado);
    List<Reporte> buscarPorCategoria(String nombreCategoria);
    List<Reporte> buscarPorCorreoReportante(String correo);
    List<IncidenciasPorMesDTO> contarPorUsuarioYMes();
    List<IncidenciasPorCampusDTO> contarPorCampusYEstado(String estado);
    List<Reporte> buscarPorPrioridad(String prioridad);
    List<Reporte> buscarPorCampus(String campus);
}
