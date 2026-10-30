package com.igarrido.store.infrastructure.persistence.repository;

import com.igarrido.store.domain.model.User;
import com.igarrido.store.domain.repository.UserRepository;
import com.igarrido.store.infrastructure.persistence.entity.UserEntity;
import com.igarrido.store.infrastructure.persistence.jpa.UserJpaRepository;
import com.igarrido.store.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador: implementa el puerto del dominio usando JPA.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity saved = jpaRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByUsernameIgnoreCase(String username) {
        return jpaRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream().map(UserMapper::toDomain).collect(Collectors.toList());
    }
}
