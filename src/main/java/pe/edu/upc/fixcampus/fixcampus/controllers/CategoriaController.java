package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.fixcampus.fixcampus.dtos.CategoriaDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.CategoriaDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.ICategoriaService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriaController {

    private final ICategoriaService categoriaService;
    private final ModelMapper modelMapper;

    public CategoriaController(
            ICategoriaService categoriaService,
            ModelMapper modelMapper) {
        this.categoriaService = categoriaService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'TECNICO')")
    public ResponseEntity<List<CategoriaDTOList>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion) {

        List<Categoria> categorias;

        if (nombre != null && !nombre.isBlank()) {
            categorias = categoriaService.buscarPorNombre(nombre);
        } else if (descripcion != null && !descripcion.isBlank()) {
            categorias = categoriaService.buscarPorDescripcion(descripcion);
        } else {
            categorias = categoriaService.listar();
        }

        List<CategoriaDTOList> lista = categorias
                .stream()
                .map(categoria ->
                        modelMapper.map(
                                categoria,
                                CategoriaDTOList.class
                        )
                )
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar-descripcion")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'TECNICO')")
    public ResponseEntity<List<CategoriaDTOList>> buscarPorDescripcion(
            @RequestParam String palabraClave) {

        List<CategoriaDTOList> lista =
                categoriaService.buscarPorDescripcion(palabraClave)
                        .stream()
                        .map(categoria ->
                                modelMapper.map(
                                        categoria,
                                        CategoriaDTOList.class
                                )
                        )
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'TECNICO')")
    public ResponseEntity<CategoriaDTOList> buscarPorId(
            @PathVariable Long id) {

        Categoria categoria = categoriaService.buscarPorId(id);

        CategoriaDTOList response =
                modelMapper.map(
                        categoria,
                        CategoriaDTOList.class
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTOList> registrar(
            @Valid @RequestBody CategoriaDTOInsert dto) {

        Categoria categoria =
                modelMapper.map(dto, Categoria.class);

        Categoria guardada =
                categoriaService.registrar(categoria);

        CategoriaDTOList response =
                modelMapper.map(
                        guardada,
                        CategoriaDTOList.class
                );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardada.getIdCategoria())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaDTOInsert dto) {

        Categoria categoria =
                modelMapper.map(dto, Categoria.class);

        Categoria actualizada =
                categoriaService.actualizar(id, categoria);

        CategoriaDTOList response =
                modelMapper.map(
                        actualizada,
                        CategoriaDTOList.class
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        categoriaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}