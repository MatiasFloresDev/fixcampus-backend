package pe.edu.upc.fixcampus.fixcampus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.IReporteService;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.dtos.IncidenciasPorMesDTO;
import pe.edu.upc.fixcampus.fixcampus.repositories.ICategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUbicacionRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IReporteRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IRolRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.IUsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReporteRepositoryTests {

    @Autowired private IReporteService reportService;

    @Autowired
    private IRolRepository roleRepository;

    @Autowired
    private IUsuarioRepository userRepository;

    @Autowired
    private ICategoriaRepository categoryRepository;

    @Autowired
    private IUbicacionRepository locationRepository;

    @Autowired
    private IReporteRepository reportRepository;


    @Test
    void debeFiltrarPorEstadoYAgruparPorUsuarioYMes() {
        Rol role = new Rol();
        role.setNombre("ESTUDIANTE");
        role.setNivelAcceso("BASICO");
        roleRepository.save(role);

        Usuario user = new Usuario();
        user.setRol(role);
        user.setNombre("Matias");
        user.setApellido("Prueba");
        user.setCorreo("matias.prueba@upc.edu.pe");
        user.setContrasenaHash("hash");
        user.setEstado("ACTIVO");
        user.setFechaRegistro(LocalDateTime.now());
        userRepository.save(user);

        Categoria category = new Categoria();
        category.setNombre("Limpieza de prueba");
        category.setDescripcion("Espacios que necesitan atención");
        categoryRepository.save(category);

        Ubicacion location = new Ubicacion();
        location.setCampus("UPC San Miguel");
        location.setEdificio("Pabellón A");
        location.setPiso(1);
        location.setZona("Aula 101");
        location.setTipo("AULA");
        locationRepository.save(location);

        Reporte report = new Reporte();
        report.setUsuarioReportante(user);
        report.setCategoria(category);
        report.setUbicacion(location);
        report.setTitulo("Luz apagada");
        report.setDescripcion("La luz del aula no enciende");
        report.setEstado("ABIERTO");
        report.setPrioridad("MEDIA");
        report.setFechaCreacion(LocalDateTime.now());
        reportRepository.save(report);

        assertThat(reportRepository.findByEstadoIgnoreCase("abierto")).hasSize(1);
        List<Reporte> porCorreo = reportRepository.findByCorreoReportante("MATIAS.PRUEBA@UPC.EDU.PE");
        assertThat(porCorreo).hasSize(1);
        assertThat(porCorreo.get(0).getTitulo()).isEqualTo("Luz apagada");

        List<IncidenciasPorMesDTO> porMes = reportService.contarPorUsuarioYMes();
        assertThat(porMes).hasSize(1);
        assertThat(porMes.get(0).getUsuarioId()).isEqualTo(user.getIdUsuario());
        assertThat(porMes.get(0).getAnio()).isEqualTo(LocalDateTime.now().getYear());
        assertThat(porMes.get(0).getMes()).isEqualTo(LocalDateTime.now().getMonthValue());
        assertThat(porMes.get(0).getCantidad()).isEqualTo(1L);

        assertThat(reportRepository.findByPrioridad("media")).hasSize(1);
        assertThat(reportRepository.findByCampus("upc san miguel")).hasSize(1);
        assertThat(reportRepository.findByCampus("otro campus")).isEmpty();
    }
}
