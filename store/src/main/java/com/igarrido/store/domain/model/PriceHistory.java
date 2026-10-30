package com.igarrido.store.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceHistory {
    private Long id;
    private Long productId;
    private BigDecimal price;
    private LocalDateTime changedAt;
}
