package com.igarrido.store.application.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String brand;
    private String category;
    private String imageUrl;
    private Integer stock;
}
