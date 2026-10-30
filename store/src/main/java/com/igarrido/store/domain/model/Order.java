package com.igarrido.store.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Order {
    private Long id;
    private String customerName;
    private String phone;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal total;
    private LocalDate orderDate;
    private String notes;
    private boolean ordered;
    private boolean prepared;
    private boolean paid;
    private String customerUsername;
    private String paymentProofUrl;
    private int modificationCount;
}
