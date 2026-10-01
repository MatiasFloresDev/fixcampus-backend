package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.fixcampus.fixcampus.dtos.UbicacionDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.UbicacionDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUbicacionService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class UbicacionController {

    private final IUbicacionService ubicacionService;
    private final ModelMapper modelMapper;

    public UbicacionController(
            IUbicacionService ubicacionService,
            ModelMapper modelMapper) {
        this.ubicacionService = ubicacionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'TECNICO')")
    public ResponseEntity<List<UbicacionDTOList>> listar(
            @RequestParam(required = false) String campus) {

        List<Ubicacion> ubicaciones;

        if (campus != null && !campus.isBlank()) {
            ubicaciones = ubicacionService.buscarPorCampus(campus);
        } else {
            ubicaciones = ubicacionService.listar();
        }

        List<UbicacionDTOList> lista = ubicaciones
                .stream()
                .map(ubicacion ->
                        modelMapper.map(
                                ubicacion,
                                UbicacionDTOList.class
                        )
                )
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'TECNICO')")
    public ResponseEntity<UbicacionDTOList> buscarPorId(
            @PathVariable Long id) {

        Ubicacion ubicacion =
                ubicacionService.buscarPorId(id);

        UbicacionDTOList response =
                modelMapper.map(
                        ubicacion,
                        UbicacionDTOList.class
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UbicacionDTOList> registrar(
            @Valid @RequestBody UbicacionDTOInsert dto) {

        Ubicacion ubicacion =
                modelMapper.map(dto, Ubicacion.class);

        Ubicacion guardada =
                ubicacionService.registrar(ubicacion);

        UbicacionDTOList response =
                modelMapper.map(
                        guardada,
                        UbicacionDTOList.class
                );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardada.getIdUbicacion())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UbicacionDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UbicacionDTOInsert dto) {

        Ubicacion ubicacion =
                modelMapper.map(dto, Ubicacion.class);

        Ubicacion actualizada =
                ubicacionService.actualizar(id, ubicacion);

        UbicacionDTOList response =
                modelMapper.map(
                        actualizada,
                        UbicacionDTOList.class
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        ubicacionService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}