package cl.ufro.redsocial.service;

import cl.ufro.redsocial.dto.RegistroForm;
import cl.ufro.redsocial.model.Usuario;

public interface AuthService {
    Usuario registrar(RegistroForm form);

    Usuario login(String email, String password);
}
