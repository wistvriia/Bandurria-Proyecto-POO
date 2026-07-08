package ufro.redsocial.controller;

import ufro.redsocial.dto.PublicacionForm;
import ufro.redsocial.model.Publicacion;
import ufro.redsocial.model.Usuario;
import ufro.redsocial.model.enums.Carrera;
import ufro.redsocial.service.PublicacionService;
import ufro.redsocial.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FeedController {

    private final PublicacionService publicacionService;
    private final UsuarioService usuarioService;

    public FeedController(PublicacionService publicacionService, UsuarioService usuarioService) {
        this.publicacionService = publicacionService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String feed(@RequestParam(required = false) String carrera,
                       @RequestParam(required = false) String q,
                       Model model) {

        Carrera filtro = Carrera.desde(carrera).orElse(null);
        boolean hayBusqueda = q != null && !q.isBlank();

        List<Publicacion> publicaciones;
        if (filtro != null) {
            publicaciones = publicacionService.porCarrera(filtro);
        } else if (hayBusqueda) {
            publicaciones = publicacionService.buscar(q);
        } else {
            publicaciones = publicacionService.feed();
        }

        List<Usuario> usuariosEncontrados = hayBusqueda ? usuarioService.buscar(q) : List.of();

        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("carreras", Carrera.listar());
        model.addAttribute("carreraSeleccionada", filtro);
        model.addAttribute("q", q);
        model.addAttribute("usuariosEncontrados", usuariosEncontrados);
        model.addAttribute("nuevaPublicacion", new PublicacionForm());
        return "feed";
    }
}
