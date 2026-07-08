package ufro.redsocial.util;

import java.util.regex.Pattern;

/**
 * Utilidad para validar que un correo pertenezca al dominio institucional UFRO.
 *
 * <p>Solo se aceptan correos terminados en {@code @ufromail.cl}; ningun otro
 * dominio es valido. Es usada por los servicios mediante una relacion de
 * DEPENDENCIA (uso de un metodo estatico, sin mantener una referencia).</p>
 */
public final class ValidadorCorreoInstitucional {

    public static final String DOMINIO = "@ufromail.cl";

    private static final Pattern PATRON =
            Pattern.compile("^[a-z0-9._%+-]+@ufromail\\.cl$");

    private ValidadorCorreoInstitucional() {
        // clase de utilidad: no instanciable
    }

    /** @return true si el correo es un correo institucional UFRO valido. */
    public static boolean esValido(String email) {
        return email != null && PATRON.matcher(email.trim().toLowerCase()).matches();
    }
}
