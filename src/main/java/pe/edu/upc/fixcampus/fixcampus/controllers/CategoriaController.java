package pe.edu.upc.fixcampus.fixcampus.controllers;

import java.util.ArrayList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.fixcampus.fixcampus.dtos.CategoriaConReportesDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.CategoriaDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.CategoriaDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.CategoriaService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriaController {

    private final CategoriaService service;
    private final ModelMapper modelMapper;

    public CategoriaController(CategoriaService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @Operation(summary = "Listar categorías", description = "Permite buscar por parte del nombre o por una palabra de la descripción.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<CategoriaDTOList>> listar(
            @Parameter(description = "Parte del nombre de la categoría") @RequestParam(required = false) String nombre,
            @Parameter(description = "Palabra que debe aparecer en la descripción") @RequestParam(required = false) String descripcion) {
        List<Categoria> categorias;
        if (nombre != null && !nombre.isBlank()) {
            categorias = service.buscarPorNombre(nombre);
        } else if (descripcion != null && !descripcion.isBlank()) {
            categorias = service.buscarPorDescripcion(descripcion);
        } else {
            categorias = service.listar();
        }
        return ResponseEntity.ok(convertirLista(categorias));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<CategoriaDTOList> buscarPorId(@PathVariable Long id) {
        Categoria categoria = service.buscarPorId(id);
        CategoriaDTOList response = modelMapper.map(categoria, CategoriaDTOList.class);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTOList> registrar(
            @Valid @RequestBody CategoriaDTOInsert dto) {

        Categoria categoria = modelMapper.map(dto, Categoria.class);
        Categoria guardada = service.registrar(categoria);
        CategoriaDTOList response = modelMapper.map(guardada, CategoriaDTOList.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardada.getIdCategoria())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }
    @GetMapping("/buscar-descripcion")
    @Operation(summary = "Buscar por descripción", description = "Busca categorías que contengan una palabra clave en su descripción.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<CategoriaDTOList>> buscarPorDescripcion(
            @Parameter(description = "Palabra clave a buscar en la descripción") @RequestParam String palabraClave) {

        List<Categoria> categorias = service.buscarPorDescripcion(palabraClave);
        List<CategoriaDTOList> lista = convertirLista(categorias);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/reporte-por-categoria")
    @Operation(
        summary = "Reportes por categoría",
        description = "Consulta 20: LEFT JOIN y COUNT. Cuenta todos los reportes por categoría e incluye las que tienen cero. No filtra por fechas. Solo administradores."
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoriaConReportesDTO>> contarReportesPorCategoria() {
        return ResponseEntity.ok(service.contarReportesPorCategoria());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaDTOInsert dto) {

        Categoria datos = modelMapper.map(dto, Categoria.class);
        Categoria actualizada = service.actualizar(id, datos);
        CategoriaDTOList response = modelMapper.map(actualizada, CategoriaDTOList.class);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private List<CategoriaDTOList> convertirLista(List<Categoria> categorias) {
        List<CategoriaDTOList> lista = new ArrayList<>();
        for (Categoria categoria : categorias) {
            lista.add(modelMapper.map(categoria, CategoriaDTOList.class));
        }
        return lista;
    }
}
