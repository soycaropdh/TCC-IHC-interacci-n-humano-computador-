package com.igarrido.store.application.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime updatedAt;
    private String brand;
    private String category;
    private String imageUrl;
    private Integer stock;
}
