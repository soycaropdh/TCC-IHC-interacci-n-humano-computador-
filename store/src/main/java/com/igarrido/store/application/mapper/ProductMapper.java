package com.igarrido.store.application.mapper;

import com.igarrido.store.application.request.ProductRequest;
import com.igarrido.store.application.response.ProductResponse;
import com.igarrido.store.domain.constants.ProductCategory;
import com.igarrido.store.domain.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public static Product toDomain(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .brand(request.getBrand())
                .category(ProductCategory.from(request.getCategory()))
                .imageUrl(request.getImageUrl())
                .stock(request.getStock())
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .updatedAt(product.getUpdatedAt())
                .brand(product.getBrand())
                .category(product.getCategory() == null ? null : product.getCategory().name())
                .imageUrl(product.getImageUrl())
                .stock(product.getStock())
                .build();
    }
}
