package pe.edu.upc.fixcampus.fixcampus.servicesinterfaces;

import pe.edu.upc.fixcampus.fixcampus.dtos.EvidenciasPorUsuarioDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface AdjuntoService {
    List<EvidenciasPorUsuarioDTO> contarEvidenciasPorUsuario(String correo);
    List<Adjunto> listar();
    List<Adjunto> buscarPorTipo(String tipo);
    List<Adjunto> buscarPorReporte(Long reporteId);
    Adjunto buscarPorId(Long id);
    Adjunto buscarPorUrl(String url);
    Reporte buscarReporte(Long id);
    Path rutaArchivo(String fileName);
    Adjunto subir(Long reporteId, MultipartFile file) throws IOException;
    Adjunto registrar(AdjuntoDTO datos);
    Adjunto actualizar(Long id, AdjuntoDTO datos);
    void eliminar(Long id);
}
