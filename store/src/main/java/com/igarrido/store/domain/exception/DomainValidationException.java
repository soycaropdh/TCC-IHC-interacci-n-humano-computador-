package com.igarrido.store.domain.exception;

/**
 * Se lanza cuando una regla de negocio no se cumple (datos inválidos).
 * Vive en el dominio: no depende de ningún framework.
 */
public class DomainValidationException extends RuntimeException {
    public DomainValidationException(String message) {
        super(message);
    }
}
