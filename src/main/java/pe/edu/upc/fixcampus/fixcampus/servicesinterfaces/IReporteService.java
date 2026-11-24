package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import java.util.List;

import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;

public interface IReporteService {

    List<Object[]> contarPorCategoria();

    List<Reporte> listar();

    Reporte buscarPorId(Long id);

    Reporte registrar(Reporte datos);

    Reporte actualizar(Long id, Reporte datos);

    void eliminar(Long id);

    List<Reporte> buscarPorEstado(String estado);

    List<Reporte> buscarPorCategoria(String nombreCategoria);

    List<Reporte> buscarPorCorreoReportante(String correo);

    List<Object[]> contarPorUsuarioYMes();

    List<Reporte> buscarPorPrioridad(String prioridad);
}