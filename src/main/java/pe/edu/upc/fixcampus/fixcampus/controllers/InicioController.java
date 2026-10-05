package pe.edu.upc.fixcampus.fixcampus.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InicioController {

    @GetMapping("/")
    public String inicio() {
        return "FixCampus API activa. Documentacion: /swagger-ui/index.html";
    }
}
