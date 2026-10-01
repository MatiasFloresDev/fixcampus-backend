package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import java.time.LocalDate;
import java.util.List;

import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCategoriaDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorCampusDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;

public interface IReporteService {

    List<IncidenciasPorCategoriaDTO> contarPorCategoriaEntreFechas(
            LocalDate desde, LocalDate hasta);

    List<Reporte> listar();

    Reporte buscarPorId(Long id);

    Reporte registrar(Reporte datos);

    Reporte actualizar(Long id, Reporte datos);

    void eliminar(Long id);

    List<Reporte> buscarPorEstado(String estado);

    List<Reporte> buscarPorCategoria(String nombreCategoria);

    List<Reporte> buscarPorCorreoReportante(String correo);

    List<IncidenciasPorMesDTO> contarPorUsuarioYMes();

    List<IncidenciasPorCampusDTO> contarPorCampusYEstado(String estado);

    List<Reporte> buscarPorPrioridad(String prioridad);

    List<Reporte> buscarPorCampus(String campus);
}