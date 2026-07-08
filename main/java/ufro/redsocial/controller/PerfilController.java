package ufro.redsocial.controller;

import ufro.redsocial.dto.CambioPasswordForm;
import ufro.redsocial.dto.EditarPerfilForm;
import ufro.redsocial.exception.RedSocialException;
import ufro.redsocial.model.Publicacion;
import ufro.redsocial.model.Usuario;
import ufro.redsocial.model.enums.Carrera;
import ufro.redsocial.service.AlmacenamientoArchivoService;
import ufro.redsocial.service.PublicacionService;
import ufro.redsocial.service.UsuarioService;
import ufro.redsocial.util.Sesion;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;
    private final PublicacionService publicacionService;
    private final AlmacenamientoArchivoService almacenamiento;

    public PerfilController(UsuarioService usuarioService,
                            PublicacionService publicacionService,
                            AlmacenamientoArchivoService almacenamiento) {
        this.usuarioService = usuarioService;
        this.publicacionService = publicacionService;
        this.almacenamiento = almacenamiento;
    }

    @GetMapping("/editar")
    public String editarForm(Model model, HttpSession session) {
        Usuario usuario = usuarioService.porId((String) session.getAttribute(Sesion.USUARIO_ID));
        EditarPerfilForm form = new EditarPerfilForm();
        form.setNombreCompleto(usuario.getNombreCompleto());
        form.setUsername(usuario.getUsername());
        form.setBiografia(usuario.getBiografia());
        form.setCarrera(usuario.getCarrera());

        model.addAttribute("editarPerfilForm", form);
        model.addAttribute("carreras", Carrera.listar());
        return "editar-perfil";
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute("editarPerfilForm") EditarPerfilForm form,
                        @RequestParam(value = "foto", required = false) MultipartFile foto,
                        HttpSession session, Model model) {
        String id = (String) session.getAttribute(Sesion.USUARIO_ID);
        try {
            Usuario usuario = usuarioService.actualizarPerfil(id, form);
            if (foto != null && !foto.isEmpty()) {
                usuarioService.actualizarFoto(id, almacenamiento.guardar(foto));
            }
            session.setAttribute(Sesion.USERNAME, usuario.getUsername());
            return "redirect:/perfil/" + usuario.getUsername();
        } catch (RedSocialException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("carreras", Carrera.listar());
            return "editar-perfil";
        }
    }

    @GetMapping("/password")
    public String passwordForm(Model model) {
        if (!model.containsAttribute("cambioPasswordForm")) {
            model.addAttribute("cambioPasswordForm", new CambioPasswordForm());
        }
        return "cambiar-password";
    }

    @PostMapping("/password")
    public String password(@ModelAttribute("cambioPasswordForm") CambioPasswordForm form,
                          HttpSession session, Model model) {
        try {
            usuarioService.cambiarPassword((String) session.getAttribute(Sesion.USUARIO_ID), form);
            model.addAttribute("exito", "Contraseña actualizada correctamente.");
        } catch (RedSocialException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("cambioPasswordForm", new CambioPasswordForm());
        return "cambiar-password";
    }

    @GetMapping("/{username}")
    public String ver(@PathVariable String username, Model model) {
        Usuario perfil = usuarioService.porUsername(username);
        List<Publicacion> publicaciones = publicacionService.feed().stream()
                .filter(p -> p.getAutorId().equals(perfil.getId()))
                .toList();

        model.addAttribute("perfil", perfil);
        model.addAttribute("publicaciones", publicaciones);
        return "perfil";
    }
}
