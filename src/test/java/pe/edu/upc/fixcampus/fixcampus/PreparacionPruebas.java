package pe.edu.upc.fixcampus.fixcampus;

import pe.edu.upc.fixcampus.fixcampus.dtos.UsuarioDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.repositories.RolRepository;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.UsuarioService;

import java.util.Optional;

// Datos exclusivos de las pruebas en H2. La aplicación no ejecuta esta clase.
class PreparacionPruebas {

    static void crearCuentas(RolRepository roles, UsuarioService usuarios) {
        String[] nombres = {"ADMIN", "USUARIO"};
        String[] correos = {"admin@fixcampus.com", "usuario@fixcampus.com"};
        String[] claves = {"admin123", "usuario123"};

        for (int i = 0; i < nombres.length; i++) {
            Optional<Rol> encontrado = roles.findByNombre(nombres[i]);
            Rol rol;
            if (encontrado.isPresent()) {
                rol = encontrado.get();
            } else {
                rol = new Rol();
                rol.setNombre(nombres[i]);
                rol.setNivelAcceso(i == 0 ? "TOTAL" : "BASICO");
                rol = roles.save(rol);
            }

            boolean existe = false;
            for (Usuario usuario : usuarios.listar()) {
                if (usuario.getCorreo().equals(correos[i])) {
                    existe = true;
                }
            }
            if (!existe) {
                UsuarioDTOInsert datos = new UsuarioDTOInsert();
                datos.setRolId(rol.getIdRol());
                datos.setNombre("Cuenta de prueba");
                datos.setApellido("FixCampus");
                datos.setCorreo(correos[i]);
                datos.setPassword(claves[i]);
                datos.setEstado("ACTIVO");
                usuarios.crear(datos);
            }
        }
    }
}
