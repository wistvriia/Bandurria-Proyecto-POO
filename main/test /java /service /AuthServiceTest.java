package ufro.redsocial.service;

import ufro.redsocial.dto.RegistroForm;
import ufro.redsocial.exception.CorreoInstitucionalInvalidoException;
import ufro.redsocial.exception.CredencialesInvalidasException;
import ufro.redsocial.exception.LongitudContrasenaInvalidaException;
import ufro.redsocial.exception.UsernameDuplicadoException;
import ufro.redsocial.model.Usuario;
import ufro.redsocial.model.enums.Carrera;
import ufro.redsocial.repository.UsuarioRepository;
import ufro.redsocial.seguridad.CifradorBCrypt;
import ufro.redsocial.seguridad.CifradorContrasena;
import ufro.redsocial.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private final CifradorContrasena cifrador = new CifradorBCrypt();
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(usuarioRepository, cifrador);
    }

    private RegistroForm formValido() {
        RegistroForm f = new RegistroForm();
        f.setNombreCompleto("Juan Pérez");
        f.setUsername("juanp");
        f.setEmail("juan@ufromail.cl");
        f.setPassword("password123");
        f.setCarrera(Carrera.INGENIERIA_INFORMATICA);
        return f;
    }

    @Test
    @DisplayName("1. Registro correcto: guarda el usuario con la contraseña cifrada")
    void registroCorrecto() {
        when(usuarioRepository.existsByEmailIgnoreCase("juan@ufromail.cl")).thenReturn(false);
        when(usuarioRepository.existsByUsernameIgnoreCase("juanp")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario creado = authService.registrar(formValido());

        assertEquals("juan@ufromail.cl", creado.getEmail());
        assertEquals("juanp", creado.getUsername());
        assertNotEquals("password123", creado.getPasswordHash(), "La contraseña debe almacenarse cifrada");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("2. Registro con correo no institucional lanza excepción")
    void registroCorreoInvalido() {
        RegistroForm f = formValido();
        f.setEmail("juan@gmail.com");

        assertThrows(CorreoInstitucionalInvalidoException.class, () -> authService.registrar(f));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("3. Registro con username duplicado lanza excepción")
    void registroUsernameDuplicado() {
        when(usuarioRepository.existsByEmailIgnoreCase("juan@ufromail.cl")).thenReturn(false);
        when(usuarioRepository.existsByUsernameIgnoreCase("juanp")).thenReturn(true);

        assertThrows(UsernameDuplicadoException.class, () -> authService.registrar(formValido()));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("4. Registro con contraseña de largo inválido lanza excepción")
    void registroLargoContrasenaInvalido() {
        RegistroForm f = formValido();
        f.setPassword("123");  // menos de 8

        assertThrows(LongitudContrasenaInvalidaException.class, () -> authService.registrar(f));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("5. Login correcto devuelve el usuario autenticado")
    void loginCorrecto() {
        Usuario u = new Usuario();
        u.setEmail("juan@ufromail.cl");
        u.setUsername("juanp");
        u.setPasswordHash(cifrador.cifrar("password123"));
        when(usuarioRepository.findByEmailIgnoreCase("juan@ufromail.cl")).thenReturn(Optional.of(u));

        Usuario logueado = authService.login("juan@ufromail.cl", "password123");

        assertEquals("juanp", logueado.getUsername());
    }

    @Test
    @DisplayName("6. Login incorrecto (correo inexistente) lanza excepción")
    void loginIncorrecto() {
        when(usuarioRepository.findByEmailIgnoreCase("noexiste@ufromail.cl")).thenReturn(Optional.empty());

        assertThrows(CredencialesInvalidasException.class,
                () -> authService.login("noexiste@ufromail.cl", "password123"));
    }
}
