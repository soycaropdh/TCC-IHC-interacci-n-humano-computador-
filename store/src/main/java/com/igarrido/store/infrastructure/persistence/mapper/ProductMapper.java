package com.igarrido.store.infrastructure.persistence.mapper;

import com.igarrido.store.domain.constants.ProductCategory;
import com.igarrido.store.domain.model.PriceHistory;
import com.igarrido.store.domain.model.Product;
import com.igarrido.store.infrastructure.persistence.entity.PriceHistoryEntity;
import com.igarrido.store.infrastructure.persistence.entity.ProductEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    public static Product toDomain(ProductEntity entity) {
        if (entity == null) return null;

        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .updatedAt(entity.getUpdatedAt())
                .brand(entity.getBrand())
                .category(toCategory(entity.getCategory()))
                .imageUrl(entity.getImageUrl())
                .stock(entity.getStock())
                .priceHistory(toDomainHistoryList(entity.getPriceHistory()))
                .build();
    }

    public static ProductEntity toEntity(Product domain) {
        if (domain == null) return null;

        ProductEntity entity = ProductEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .price(domain.getPrice())
                .updatedAt(domain.getUpdatedAt())
                .brand(domain.getBrand())
                .category(domain.getCategory() == null ? null : domain.getCategory().name())
                .imageUrl(domain.getImageUrl())
                .stock(domain.getStock())
                .build();

        if (domain.getPriceHistory() != null) {
            List<PriceHistoryEntity> historyEntities =
                    domain.getPriceHistory().stream()
                            .map(h -> PriceHistoryEntity.builder()
                                    .id(h.getId())
                                    .product(entity)
                                    .price(h.getPrice())
                                    .changedAt(h.getChangedAt())
                                    .build())
                            .collect(Collectors.toList());
            entity.setPriceHistory(historyEntities);
        }

        return entity;
    }

    private static ProductCategory toCategory(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return ProductCategory.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return null; // valor desconocido en la base: se muestra como "sin categoría"
        }
    }

    private static List<PriceHistory> toDomainHistoryList(List<PriceHistoryEntity> entities) {
        if (entities == null) return null;

        return entities.stream()
                .map(e -> PriceHistory.builder()
                        .id(e.getId())
                        .productId(e.getProduct().getId())
                        .price(e.getPrice())
                        .changedAt(e.getChangedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
