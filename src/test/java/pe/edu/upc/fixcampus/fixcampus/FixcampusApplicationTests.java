package pe.edu.upc.fixcampus.fixcampus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pe.edu.upc.fixcampus.fixcampus.repositories.IRolRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUsuarioRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.ICategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUbicacionRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:fixcampus-arranque;DB_CLOSE_DELAY=-1")
class FixcampusApplicationTests {

    @Autowired private IRolRepository roles;
    @Autowired private IUsuarioRepository usuarios;
    @Autowired private ICategoriaRepository categorias;
    @Autowired private IUbicacionRepository ubicaciones;

    @Test
    void arrancaSinCrearDatosAutomaticos() {
        assertThat(roles.count()).isZero();
        assertThat(usuarios.count()).isZero();
        assertThat(categorias.count()).isZero();
        assertThat(ubicaciones.count()).isZero();
    }

}
