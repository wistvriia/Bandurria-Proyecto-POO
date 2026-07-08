package ufro.redsocial.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio de almacenamiento de archivos (imagenes de perfil y publicaciones).
 * Tecnologia extra adicional: subida de imagenes.
 */
public interface AlmacenamientoArchivoService {

    /**
     * Guarda el archivo recibido y devuelve la URL publica para mostrarlo.
     * @return URL relativa (p. ej. {@code /uploads/uuid.png}) o {@code null} si no hay archivo.
     */
    String guardar(MultipartFile archivo);
}
