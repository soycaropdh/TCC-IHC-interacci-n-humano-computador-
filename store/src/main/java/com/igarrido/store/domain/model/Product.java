package com.igarrido.store.domain.model;

import com.igarrido.store.domain.constants.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime updatedAt;

    // Datos de electrodomésticos
    private String brand;
    private ProductCategory category;
    private String imageUrl;
    private Integer stock;

    // Lista de historicos de precio, opcional para que el servicio pueda manejarlo
    private List<PriceHistory> priceHistory;
}
