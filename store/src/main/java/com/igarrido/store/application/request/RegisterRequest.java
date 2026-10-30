package com.igarrido.store.application.request;

import lombok.Data;

/**
 * Datos para crear una cuenta. Fíjate que NO hay campo "role":
 * el rol lo decide el dominio, nunca el cliente.
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
}
