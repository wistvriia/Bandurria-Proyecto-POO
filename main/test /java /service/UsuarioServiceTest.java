package cl.ufro.redsocial.service;

import cl.ufro.redsocial.dto.CambioPasswordForm;
import cl.ufro.redsocial.exception.ContrasenaActualIncorrectaException;
import cl.ufro.redsocial.model.Usuario;
import cl.ufro.redsocial.model.enums.Carrera;
import cl.ufro.redsocial.repository.UsuarioRepository;
import cl.ufro.redsocial.seguridad.CifradorBCrypt;
import cl.ufro.redsocial.seguridad.CifradorContrasena;
import cl.ufro.redsocial.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas del servicio de usuarios (cambio de contraseña y búsqueda).
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private final CifradorContrasena cifrador = new CifradorBCrypt();
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioServiceImpl(usuarioRepository, cifrador);
    }

    @Test
    @DisplayName("7. Cambio de contraseña correcto actualiza el hash")
    void cambioPasswordCorrecto() {
        Usuario u = new Usuario();
        u.setId("u1");
        u.setPasswordHash(cifrador.cifrar("actual1234"));
        when(usuarioRepository.findById("u1")).thenReturn(Optional.of(u));

        CambioPasswordForm form = new CambioPasswordForm();
        form.setPasswordActual("actual1234");
        form.setPasswordNueva("nueva12345");

        usuarioService.cambiarPassword("u1", form);

        assertTrue(cifrador.coincide("nueva12345", u.getPasswordHash()));
        verify(usuarioRepository).save(u);
    }

    @Test
    @DisplayName("8. Cambio de contraseña con actual incorrecta lanza excepción")
    void cambioPasswordIncorrecto() {
        Usuario u = new Usuario();
        u.setId("u1");
        u.setPasswordHash(cifrador.cifrar("actual1234"));
        when(usuarioRepository.findById("u1")).thenReturn(Optional.of(u));

        CambioPasswordForm form = new CambioPasswordForm();
        form.setPasswordActual("equivocada");
        form.setPasswordNueva("nueva12345");

        assertThrows(ContrasenaActualIncorrectaException.class,
                () -> usuarioService.cambiarPassword("u1", form));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("9. Búsqueda de usuarios filtra por nombre o username")
    void buscarUsuarios() {
        Usuario ana = usuario("ana", "Ana Torres", Carrera.PSICOLOGIA);
        Usuario luis = usuario("luis", "Luis Soto", Carrera.DERECHO);
        Usuario mariana = usuario("mariana", "Mariana Díaz", Carrera.MEDICINA);
        when(usuarioRepository.findAll()).thenReturn(List.of(ana, luis, mariana));

        List<Usuario> resultado = usuarioService.buscar("ana");

        // "ana" coincide con username "ana" y con "Mariana"
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(ana));
        assertTrue(resultado.contains(mariana));
    }

    private Usuario usuario(String username, String nombre, Carrera carrera) {
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setNombreCompleto(nombre);
        u.setCarrera(carrera);
        return u;
    }
}
