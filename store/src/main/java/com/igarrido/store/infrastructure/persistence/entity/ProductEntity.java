package com.igarrido.store.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Columnas nuevas: Hibernate las agrega solo (ddl-auto=update) y quedan en NULL
    // para los productos que ya existían.
    private String brand;

    private String category;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    private Integer stock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceHistoryEntity> priceHistory;
}
