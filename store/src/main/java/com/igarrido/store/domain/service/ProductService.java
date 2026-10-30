package com.igarrido.store.domain.service;

import com.igarrido.store.domain.constants.ProductTypeChange;
import com.igarrido.store.domain.exception.DomainValidationException;
import com.igarrido.store.domain.model.Product;
import com.igarrido.store.domain.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductRepository productRepository;
    private final WebSocketCommunicationService wsService;

    public ProductService(ProductRepository productRepository, WebSocketCommunicationService wsService) {
        this.productRepository = productRepository;
        this.wsService = wsService;
    }

    public Product createProduct(Product product) {
        normalizeAndValidate(product);
        Product save = productRepository.save(product);
        wsService.nofifyChanges(ProductTypeChange.NEW, save);
        return save;
    }

    public Optional<Product> getProduct(Long id) { return productRepository.findById(id); }
    public List<Product> getAllProducts() { return productRepository.findAll(); }

    public Product updateProduct(Product product) {
        if (product.getId() == null || productRepository.findById(product.getId()).isEmpty()) {
            throw new IllegalArgumentException("El producto no existe");
        }
        normalizeAndValidate(product);
        product.setUpdatedAt(LocalDateTime.now());
        Product updated = productRepository.save(product);
        wsService.nofifyChanges(ProductTypeChange.UPDATED, updated);
        return updated;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        wsService.nofifyChanges(ProductTypeChange.DELETED, Product.builder().id(id).build());
    }

    private void normalizeAndValidate(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new DomainValidationException("El nombre del dedito es obligatorio");
        }
        product.setName(product.getName().trim());
        if (product.getName().length() > 120) {
            throw new DomainValidationException("El nombre no puede superar 120 caracteres");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainValidationException("El precio debe ser 0 o mayor");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new DomainValidationException("La cantidad disponible debe ser 0 o mayor");
        }
        if (product.getDescription() != null && product.getDescription().length() > 255) {
            throw new DomainValidationException("La descripción no puede superar 255 caracteres");
        }
        product.setBrand("DeliDeditos by Caro");
        if (product.getUpdatedAt() == null) product.setUpdatedAt(LocalDateTime.now());
    }
}
