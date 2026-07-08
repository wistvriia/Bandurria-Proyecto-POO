package ufro.redsocial.controller;

import ufro.redsocial.dto.LoginForm;
import ufro.redsocial.dto.RegistroForm;
import ufro.redsocial.exception.CredencialesInvalidasException;
import ufro.redsocial.exception.RedSocialException;
import ufro.redsocial.model.Usuario;
import ufro.redsocial.model.enums.Carrera;
import ufro.redsocial.service.AuthService;
import ufro.redsocial.util.Sesion;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm form, HttpSession session, Model model) {
        try {
            Usuario usuario = authService.login(form.getEmail(), form.getPassword());
            session.setAttribute(Sesion.USUARIO_ID, usuario.getId());
            session.setAttribute(Sesion.USERNAME, usuario.getUsername());
            return "redirect:/";
        } catch (CredencialesInvalidasException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        if (!model.containsAttribute("registroForm")) {
            model.addAttribute("registroForm", new RegistroForm());
        }
        model.addAttribute("carreras", Carrera.listar());
        return "registro";
    }

    @PostMapping("/registro")
    public String registro(@ModelAttribute("registroForm") RegistroForm form, HttpSession session, Model model) {
        try {
            Usuario usuario = authService.registrar(form);
            session.setAttribute(Sesion.USUARIO_ID, usuario.getId());
            session.setAttribute(Sesion.USERNAME, usuario.getUsername());
            return "redirect:/";
        } catch (RedSocialException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("carreras", Carrera.listar());
            return "registro";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
