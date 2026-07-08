package cl.ufro.redsocial.service;

import cl.ufro.redsocial.dto.PublicacionForm;
import cl.ufro.redsocial.exception.OperacionNoAutorizadaException;
import cl.ufro.redsocial.model.Comentario;
import cl.ufro.redsocial.model.Publicacion;
import cl.ufro.redsocial.model.Respuesta;
import cl.ufro.redsocial.model.enums.Carrera;
import cl.ufro.redsocial.model.enums.TipoReaccion;
import cl.ufro.redsocial.repository.PublicacionRepository;
import cl.ufro.redsocial.service.impl.PublicacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicacionServiceTest {

    @Mock
    private PublicacionRepository publicacionRepository;

    private PublicacionService publicacionService;

    @BeforeEach
    void setUp() {
        publicacionService = new PublicacionServiceImpl(publicacionRepository);
    }

    private Publicacion publicacionDe(String autorId, String autorUsername) {
        Publicacion p = new Publicacion();
        p.setId(UUID.randomUUID().toString());
        p.setAutorId(autorId);
        p.setAutorUsername(autorUsername);
        p.setTexto("Texto de prueba");
        return p;
    }

    @Test
    @DisplayName("10. Crear publicación guarda los datos y el tag de carrera")
    void crearPublicacion() {
        when(publicacionRepository.save(any(Publicacion.class))).thenAnswer(inv -> inv.getArgument(0));

        PublicacionForm form = new PublicacionForm();
        form.setTexto("Mi primera publicación");
        form.setCarreraTag(Carrera.INGENIERIA_INFORMATICA);

        Publicacion creada = publicacionService.crear("u1", "juanp", form, null);

        assertEquals("Mi primera publicación", creada.getTexto());
        assertEquals(Carrera.INGENIERIA_INFORMATICA, creada.getCarreraTag());
        assertEquals("u1", creada.getAutorId());
        verify(publicacionRepository).save(any(Publicacion.class));
    }

    @Test
    @DisplayName("11. Editar una publicación ajena lanza OperacionNoAutorizadaException")
    void editarPublicacionAjena() {
        Publicacion p = publicacionDe("otroUsuario", "otro");
        when(publicacionRepository.findById(p.getId())).thenReturn(Optional.of(p));

        PublicacionForm form = new PublicacionForm();
        form.setTexto("Editado");

        assertThrows(OperacionNoAutorizadaException.class,
                () -> publicacionService.editar(p.getId(), "yo", form));
        verify(publicacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("12. Agregar un comentario lo añade a la publicación")
    void agregarComentario() {
        Publicacion p = publicacionDe("u1", "juanp");
        when(publicacionRepository.findById(p.getId())).thenReturn(Optional.of(p));

        Comentario c = publicacionService.comentar(p.getId(), "u2", "ana", "¡Buen aporte!");

        assertEquals("¡Buen aporte!", c.getTexto());
        assertEquals(1, p.getComentarios().size());
        verify(publicacionRepository).save(p);
    }

    @Test
    @DisplayName("13. Agregar una respuesta a un comentario (tercer nivel)")
    void agregarRespuesta() {
        Publicacion p = publicacionDe("u1", "juanp");
        Comentario c = new Comentario();
        c.setId("c1");
        c.setAutorUsername("ana");
        c.setTexto("Comentario");
        p.getComentarios().add(c);
        when(publicacionRepository.findById(p.getId())).thenReturn(Optional.of(p));

        Respuesta r = publicacionService.responder(p.getId(), "c1", "u3", "luis", "Totalmente de acuerdo");

        assertEquals("Totalmente de acuerdo", r.getTexto());
        assertEquals(1, c.getRespuestas().size());
        verify(publicacionRepository).save(p);
    }

    @Test
    @DisplayName("14. Reaccionar con Like incrementa el conteo y no se duplica")
    void reaccionarLike() {
        Publicacion p = publicacionDe("u1", "juanp");
        when(publicacionRepository.findById(p.getId())).thenReturn(Optional.of(p));
        when(publicacionRepository.save(any(Publicacion.class))).thenAnswer(inv -> inv.getArgument(0));

        publicacionService.reaccionar(p.getId(), "u2", TipoReaccion.LIKE);
        // misma reacción del mismo usuario no debe duplicar (se mantiene en 1 tras re-aplicar y quitar)
        Publicacion resultado = publicacionService.reaccionar(p.getId(), "u3", TipoReaccion.LIKE);

        assertEquals(2, resultado.contarLikes());
        assertEquals(0, resultado.contarDislikes());
    }

    @Test
    @DisplayName("15. Filtrar publicaciones por carrera usa solo el tag indicado")
    void filtrarPorCarrera() {
        Publicacion p1 = publicacionDe("u1", "juanp");
        p1.setCarreraTag(Carrera.DERECHO);
        when(publicacionRepository.findByCarreraTagOrderByFechaCreacionDesc(Carrera.DERECHO))
                .thenReturn(List.of(p1));

        List<Publicacion> resultado = publicacionService.porCarrera(Carrera.DERECHO);

        assertEquals(1, resultado.size());
        assertEquals(Carrera.DERECHO, resultado.get(0).getCarreraTag());
    }
}
