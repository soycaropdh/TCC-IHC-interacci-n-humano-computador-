package com.igarrido.store.infrastructure.persistence.jpa;
import com.igarrido.store.infrastructure.persistence.entity.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MaterialJpaRepository extends JpaRepository<MaterialEntity, Long> { boolean existsByNameIgnoreCase(String name); }
