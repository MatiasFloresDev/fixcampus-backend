package pe.edu.upc.fixcampus.fixcampus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pe.edu.upc.fixcampus.fixcampus.repositories.RolRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.CategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UbicacionRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:fixcampus-arranque;DB_CLOSE_DELAY=-1")
class FixcampusApplicationTests {

    @Autowired private RolRepository roles;
    @Autowired private UsuarioRepository usuarios;
    @Autowired private CategoriaRepository categorias;
    @Autowired private UbicacionRepository ubicaciones;

    @Test
    void arrancaSinCrearDatosAutomaticos() {
        assertThat(roles.count()).isZero();
        assertThat(usuarios.count()).isZero();
        assertThat(categorias.count()).isZero();
        assertThat(ubicaciones.count()).isZero();
    }

}
