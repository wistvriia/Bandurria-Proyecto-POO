package ufro.redsocial.exception;

public abstract class RedSocialException extends RuntimeException {

    protected RedSocialException(String mensaje) {
        super(mensaje);
    }
}
