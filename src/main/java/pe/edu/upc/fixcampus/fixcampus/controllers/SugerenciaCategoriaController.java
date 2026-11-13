package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.fixcampus.fixcampus.dtos.SugerenciaCategoriaDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.SugerenciaCategoriaDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.ISugerenciaCategoriaService;

@RestController
@RequestMapping("/api/reports/sugerir-categoria")
public class SugerenciaCategoriaController {

    private final ISugerenciaCategoriaService service;

    public SugerenciaCategoriaController(ISugerenciaCategoriaService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @Operation(summary = "Sugerir una categoría del catálogo con IA",
            description = "Para la demo usa descripciones ficticias. Devuelve un ID o null si no hay sugerencia.")
    public ResponseEntity<SugerenciaCategoriaDTO> sugerir(@Valid @RequestBody SugerenciaCategoriaDTOInsert datos) {
        return ResponseEntity.ok(service.sugerir(datos.getDescripcion()));
    }
}
