package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
        List<CategoriaDTOList> lista = categorias
                .stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaDTOList.class))
                .toList();

        return ResponseEntity.ok(lista);
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
}
