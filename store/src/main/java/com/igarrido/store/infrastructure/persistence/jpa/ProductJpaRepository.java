package com.igarrido.store.infrastructure.persistence.jpa;

import com.igarrido.store.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByNameIgnoreCase(String name);
}
