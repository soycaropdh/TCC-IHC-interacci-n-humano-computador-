package com.igarrido.store.domain.repository;

import com.igarrido.store.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUsername(String username);
    boolean existsByUsernameIgnoreCase(String username);
    List<User> findAll();
}
