package com.igarrido.store.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Table(name="materials")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class MaterialEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private String name;
    @Column(nullable=false) private String unit;
    @Column(nullable=false, precision=12, scale=3) private BigDecimal stock;
    @Column(precision=12, scale=2) private BigDecimal price;
    @Column(name="batch_consumption", nullable=false, precision=12, scale=3) private BigDecimal batchConsumption;
    @Column(name="min_stock", nullable=false, precision=12, scale=3) private BigDecimal minStock;
}
