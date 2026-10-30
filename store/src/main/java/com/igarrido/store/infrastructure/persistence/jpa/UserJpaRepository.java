package com.igarrido.store.infrastructure.persistence.jpa;

import com.igarrido.store.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsernameIgnoreCase(String username);
}
