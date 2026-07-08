package ufro.redsocial.controller;

import ufro.redsocial.model.Usuario;
import ufro.redsocial.model.enums.Carrera;
import ufro.redsocial.repository.UsuarioRepository;
import ufro.redsocial.util.Sesion;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;


@ControllerAdvice //Quiere deir que se aplica para todos los Controladores/Controllers
public class ModelGlobalAdvice {
    private final UsuarioRepository usuarioRepository;

    public ModelGlobalAdvice(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute("usuarioActual")
    public Usuario usuarioActual(HttpSession session) {
        Object id = session.getAttribute(Sesion.USUARIO_ID);
        if (id == null) {
            return null;
        }
        return usuarioRepository.findById(id.toString()).orElse(null);
    }

    @ModelAttribute("carrerasMenu")
    public List<Carrera> carrerasMenu() {
        return Carrera.listar();
    }
}
