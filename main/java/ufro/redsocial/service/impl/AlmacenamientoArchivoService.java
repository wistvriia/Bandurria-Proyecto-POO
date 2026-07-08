package cl.ufro.redsocial.service;

import org.springframework.web.multipart.MultipartFile;

public interface AlmacenamientoArchivoService {
    String guardar(MultipartFile archivo);
}
