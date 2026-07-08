package cl.ufro.redsocial.service;

import cl.ufro.redsocial.dto.PublicacionForm;
import cl.ufro.redsocial.model.Comentario;
import cl.ufro.redsocial.model.Publicacion;
import cl.ufro.redsocial.model.Respuesta;
import cl.ufro.redsocial.model.enums.Carrera;
import cl.ufro.redsocial.model.enums.TipoReaccion;

import java.util.List;

public interface PublicacionService {

    Publicacion crear(String autorId, String autorUsername, PublicacionForm form, String imagenUrl);

    Publicacion porId(String id);

    List<Publicacion> feed();

    List<Publicacion> porCarrera(Carrera carrera);

    List<Publicacion> buscar(String q);

    Publicacion editar(String publicacionId, String usuarioId, PublicacionForm form);

    void eliminar(String publicacionId, String usuarioId);

    Comentario comentar(String publicacionId, String autorId, String autorUsername, String texto);

    Respuesta responder(String publicacionId, String comentarioId,
                        String autorId, String autorUsername, String texto);

    Publicacion reaccionar(String publicacionId, String usuarioId, TipoReaccion tipo);
}
