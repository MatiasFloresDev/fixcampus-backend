package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.RolDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.RolDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IRolService;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    private final IRolService service;
    private final ModelMapper modelMapper;

    public RolController(IRolService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar roles", description = "Muestra todos los registros sin parámetros.")
    public List<RolDTOList> listar() {
        List<RolDTOList> lista = new ArrayList<>();
        for (Rol registro : service.listar()) {
            lista.add(modelMapper.map(registro, RolDTOList.class));
        }
        return lista;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RolDTOList buscar(@PathVariable Long id) {
        return modelMapper.map(service.buscarPorId(id), RolDTOList.class);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolDTOList> crear(@Valid @RequestBody RolDTOInsert datos) {
        Rol registro = modelMapper.map(datos, Rol.class);
        Rol guardado = service.registrar(registro);
        RolDTOList respuesta = modelMapper.map(guardado, RolDTOList.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RolDTOList actualizar(@PathVariable Long id, @Valid @RequestBody RolDTOInsert datos) {
        Rol registro = modelMapper.map(datos, Rol.class);
        return modelMapper.map(service.actualizar(id, registro), RolDTOList.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
