package ufro.redsocial.seguridad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CifradorBCryptTest {

    private final CifradorContrasena cifrador = new CifradorBCrypt();

    @Test
    @DisplayName("16. El hash difiere del texto plano y coincide() valida correctamente")
    void cifrarYCoincidir() {
        String plano = "miClave123";
        String hash = cifrador.cifrar(plano);

        assertNotEquals(plano, hash, "El hash no debe ser igual al texto plano");
        assertTrue(cifrador.coincide(plano, hash), "La clave correcta debe coincidir");
        assertFalse(cifrador.coincide("otraClave", hash), "Una clave distinta no debe coincidir");
    }
}
