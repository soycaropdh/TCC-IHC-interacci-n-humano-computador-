package com.igarrido.store.domain.port;

/**
 * Puerto de salida para cifrar contraseñas. El dominio no sabe si se usa BCrypt u otro.
 */
public interface PasswordHasher {
    String hash(String rawPassword);
}
