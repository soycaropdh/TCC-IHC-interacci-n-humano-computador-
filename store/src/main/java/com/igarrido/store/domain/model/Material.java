package com.igarrido.store.domain.model;

import lombok.*;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Material {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal stock;
    private BigDecimal price;
    private BigDecimal batchConsumption;
    private BigDecimal minStock;
}
