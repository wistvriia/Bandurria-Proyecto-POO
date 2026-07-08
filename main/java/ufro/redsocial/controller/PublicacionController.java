package ufro.redsocial.controller;

import ufro.redsocial.dto.PublicacionForm;
import ufro.redsocial.exception.OperacionNoAutorizadaException;
import ufro.redsocial.model.Publicacion;
import ufro.redsocial.model.enums.Carrera;
import ufro.redsocial.model.enums.TipoReaccion;
import ufro.redsocial.service.AlmacenamientoArchivoService;
import ufro.redsocial.service.PublicacionService;
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

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    private final PublicacionService publicacionService;
    private final AlmacenamientoArchivoService almacenamiento;

    public PublicacionController(PublicacionService publicacionService,
                                 AlmacenamientoArchivoService almacenamiento) {
        this.publicacionService = publicacionService;
        this.almacenamiento = almacenamiento;
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute("nuevaPublicacion") PublicacionForm form,
                        @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                        HttpSession session) {
        String autorId = (String) session.getAttribute(Sesion.USUARIO_ID);
        String autorUsername = (String) session.getAttribute(Sesion.USERNAME);
        String imagenUrl = almacenamiento.guardar(imagen);
        publicacionService.crear(autorId, autorUsername, form, imagenUrl);
        return "redirect:/";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable String id, Model model, HttpSession session) {
        Publicacion publicacion = publicacionService.porId(id);
        String autorId = (String) session.getAttribute(Sesion.USUARIO_ID);
        if (!publicacion.getAutorId().equals(autorId)) {
            throw new OperacionNoAutorizadaException("No puedes editar una publicación que no es tuya.");
        }
        PublicacionForm form = new PublicacionForm();
        form.setTexto(publicacion.getTexto());
        form.setCarreraTag(publicacion.getCarreraTag());

        model.addAttribute("publicacionForm", form);
        model.addAttribute("publicacionId", id);
        model.addAttribute("carreras", Carrera.listar());
        return "publicacion-form";
    }

    @PostMapping("/{id}/editar")
    public String editar(@PathVariable String id,
                        @ModelAttribute("publicacionForm") PublicacionForm form,
                        HttpSession session) {
        String autorId = (String) session.getAttribute(Sesion.USUARIO_ID);
        publicacionService.editar(id, autorId, form);
        return "redirect:/";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable String id, HttpSession session) {
        String autorId = (String) session.getAttribute(Sesion.USUARIO_ID);
        publicacionService.eliminar(id, autorId);
        return "redirect:/";
    }

    @PostMapping("/{id}/comentar")
    public String comentar(@PathVariable String id, @RequestParam String texto, HttpSession session) {
        publicacionService.comentar(id,
                (String) session.getAttribute(Sesion.USUARIO_ID),
                (String) session.getAttribute(Sesion.USERNAME),
                texto);
        return "redirect:/#pub-" + id;
    }

    @PostMapping("/{id}/comentarios/{comentarioId}/responder")
    public String responder(@PathVariable String id,
                           @PathVariable String comentarioId,
                           @RequestParam String texto,
                           HttpSession session) {
        publicacionService.responder(id, comentarioId,
                (String) session.getAttribute(Sesion.USUARIO_ID),
                (String) session.getAttribute(Sesion.USERNAME),
                texto);
        return "redirect:/#pub-" + id;
    }

    @PostMapping("/{id}/reaccionar")
    public String reaccionar(@PathVariable String id,
                            @RequestParam TipoReaccion tipo,
                            HttpSession session) {
        publicacionService.reaccionar(id, (String) session.getAttribute(Sesion.USUARIO_ID), tipo);
        return "redirect:/#pub-" + id;
    }
}
