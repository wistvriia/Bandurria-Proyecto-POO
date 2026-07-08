package ufro.redsocial.util;

/**
 * Claves de los atributos guardados en la {@code HttpSession} para la
 * autenticacion propia (login simple por sesion, sin Spring Security).
 */
public final class Sesion {

    public static final String USUARIO_ID = "usuarioId";
    public static final String USERNAME = "usuarioUsername";

    private Sesion() {
    }
}
