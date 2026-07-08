package cl.ufro.redsocial.service;

import cl.ufro.redsocial.dto.CambioPasswordForm;
import cl.ufro.redsocial.dto.EditarPerfilForm;
import cl.ufro.redsocial.model.Usuario;

import java.util.List;

public interface UsuarioService {

    Usuario porId(String id);

    Usuario porUsername(String username);

    List<Usuario> buscar(String q);

    Usuario actualizarPerfil(String usuarioId, EditarPerfilForm form);

    void cambiarPassword(String usuarioId, CambioPasswordForm form);

    Usuario actualizarFoto(String usuarioId, String fotoUrl);
}
