package cl.ufro.redsocial.service.impl;

import cl.ufro.redsocial.service.AlmacenamientoArchivoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Implementacion de almacenamiento en el sistema de archivos local. Las imagenes
 * se guardan en la carpeta configurada ({@code app.upload-dir}) y se exponen via
 * el handler {@code /uploads/**} registrado en {@code WebConfig}.
 */
@Service
public class AlmacenamientoArchivoServiceImpl implements AlmacenamientoArchivoService {

    private final String uploadDir;

    public AlmacenamientoArchivoServiceImpl(@Value("${app.upload-dir:uploads}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public String guardar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }
        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            String original = StringUtils.cleanPath(
                    archivo.getOriginalFilename() == null ? "archivo" : archivo.getOriginalFilename());
            String extension = original.contains(".")
                    ? original.substring(original.lastIndexOf('.'))
                    : "";
            String nombreUnico = UUID.randomUUID() + extension;

            Path destino = dir.resolve(nombreUnico);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + nombreUnico;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la imagen: " + e.getMessage(), e);
        }
    }
}
