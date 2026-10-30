package com.igarrido.store.domain.constants;

import com.igarrido.store.domain.exception.DomainValidationException;

public enum ProductCategory {
    QUESO,
    BOCADILLO,
    MIXTO,
    JAMON;

    public static ProductCategory from(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return ProductCategory.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new DomainValidationException("Categoría no válida: " + value);
        }
    }
}
