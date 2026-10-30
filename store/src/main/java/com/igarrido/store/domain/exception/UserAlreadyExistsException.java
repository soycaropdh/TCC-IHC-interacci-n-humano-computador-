package com.igarrido.store.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("El usuario '" + username + "' ya existe, elige otro nombre");
    }
}
