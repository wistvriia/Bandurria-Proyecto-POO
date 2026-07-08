package cl.ufro.redsocial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String recursoNoEncontrado(RecursoNoEncontradoException ex, Model model) {
        model.addAttribute("codigo", 404);
        model.addAttribute("titulo", "Recurso no encontrado");
        model.addAttribute("mensaje", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(OperacionNoAutorizadaException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String operacionNoAutorizada(OperacionNoAutorizadaException ex, Model model) {
        model.addAttribute("codigo", 403);
        model.addAttribute("titulo", "Operación no autorizada");
        model.addAttribute("mensaje", ex.getMessage());
        return "error";
    }
}
