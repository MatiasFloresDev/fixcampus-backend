package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.fixcampus.fixcampus.dtos.RolDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.RolDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IRolService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final IRolService rolService;
    private final ModelMapper modelMapper;

    public RolController(
            IRolService rolService,
            ModelMapper modelMapper) {
        this.rolService = rolService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RolDTOList>> listar(
            @RequestParam(required = false) String nombre) {

        List<Rol> roles;

        if (nombre != null && !nombre.isBlank()) {
            roles = rolService.buscarPorNombre(nombre);
        } else {
            roles = rolService.listar();
        }

        List<RolDTOList> lista = roles
                .stream()
                .map(rol ->
                        modelMapper.map(
                                rol,
                                RolDTOList.class
                        )
                )
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolDTOList> buscarPorId(
            @PathVariable Long id) {

        Rol rol = rolService.buscarPorId(id);

        RolDTOList response =
                modelMapper.map(
                        rol,
                        RolDTOList.class
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolDTOList> registrar(
            @Valid @RequestBody RolDTOInsert dto) {

        Rol rol =
                modelMapper.map(dto, Rol.class);

        Rol guardado =
                rolService.registrar(rol);

        RolDTOList response =
                modelMapper.map(
                        guardado,
                        RolDTOList.class
                );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getIdRol())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RolDTOInsert dto) {

        Rol rol =
                modelMapper.map(dto, Rol.class);

        Rol actualizado =
                rolService.actualizar(id, rol);

        RolDTOList response =
                modelMapper.map(
                        actualizado,
                        RolDTOList.class
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        rolService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}