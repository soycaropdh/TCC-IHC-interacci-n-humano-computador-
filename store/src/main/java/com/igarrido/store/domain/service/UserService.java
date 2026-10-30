package com.igarrido.store.domain.service;

import com.igarrido.store.domain.exception.DomainValidationException;
import com.igarrido.store.domain.exception.UserAlreadyExistsException;
import com.igarrido.store.domain.model.User;
import com.igarrido.store.domain.port.PasswordHasher;
import com.igarrido.store.domain.repository.UserRepository;

import java.util.regex.Pattern;
import java.util.List;

/**
 * Reglas de negocio de los usuarios. Clase Java pura (sin Spring).
 */
public class UserService {

    /** Todo usuario que se registra solo es cliente: nunca administrador. */
    public static final String DEFAULT_ROLE = "SHOP";

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9._-]{3,20}$");
    private static final int PASSWORD_MIN = 6;
    private static final int PASSWORD_MAX = 72; // límite real de BCrypt

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User register(String username, String rawPassword) {
        String cleanUsername = username == null ? "" : username.trim();

        if (!USERNAME_PATTERN.matcher(cleanUsername).matches()) {
            throw new DomainValidationException(
                    "El usuario debe tener entre 3 y 20 caracteres (letras, números, punto, guion o guion bajo)");
        }
        if (rawPassword == null || rawPassword.length() < PASSWORD_MIN || rawPassword.length() > PASSWORD_MAX) {
            throw new DomainValidationException(
                    "La contraseña debe tener entre " + PASSWORD_MIN + " y " + PASSWORD_MAX + " caracteres");
        }
        if (userRepository.existsByUsernameIgnoreCase(cleanUsername)) {
            throw new UserAlreadyExistsException(cleanUsername);
        }

        User user = User.builder()
                .username(cleanUsername)
                .password(passwordHasher.hash(rawPassword))
                .role(DEFAULT_ROLE)
                .build();

        return userRepository.save(user);
    }
}
