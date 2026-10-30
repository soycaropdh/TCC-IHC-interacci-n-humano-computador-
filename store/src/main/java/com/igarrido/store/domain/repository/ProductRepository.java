package com.igarrido.store.domain.repository;

import com.igarrido.store.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findByNameIgnoreCase(String name);
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
    long count();
}
