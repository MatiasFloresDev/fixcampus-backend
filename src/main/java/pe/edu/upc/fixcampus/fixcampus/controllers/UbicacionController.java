package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.UbicacionDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.UbicacionDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IUbicacionService;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class UbicacionController {
    private final IUbicacionService service;
    private final ModelMapper modelMapper;

    public UbicacionController(IUbicacionService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @Operation(summary = "Listar ubicaciones", description = "Muestra todos los registros sin parámetros.")
    public List<UbicacionDTOList> listar() {
        List<UbicacionDTOList> lista = new ArrayList<>();
        for (Ubicacion registro : service.listar()) {
            lista.add(modelMapper.map(registro, UbicacionDTOList.class));
        }
        return lista;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public UbicacionDTOList buscar(@PathVariable Long id) {
        return modelMapper.map(service.buscarPorId(id), UbicacionDTOList.class);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UbicacionDTOList> crear(@Valid @RequestBody UbicacionDTOInsert datos) {
        Ubicacion registro = modelMapper.map(datos, Ubicacion.class);
        Ubicacion guardado = service.registrar(registro);
        UbicacionDTOList respuesta = modelMapper.map(guardado, UbicacionDTOList.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UbicacionDTOList actualizar(@PathVariable Long id, @Valid @RequestBody UbicacionDTOInsert datos) {
        Ubicacion registro = modelMapper.map(datos, Ubicacion.class);
        return modelMapper.map(service.actualizar(id, registro), UbicacionDTOList.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
