package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.dtos.SugerenciaCategoriaDTO;

public interface ISugerenciaCategoriaService {

    SugerenciaCategoriaDTO sugerir(String descripcion);
}
