package com.igarrido.store.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name="orders")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="customer_name", nullable=false) private String customerName;
    private String phone;
    @Column(name="product_id", nullable=false) private Long productId;
    @Column(name="product_name", nullable=false) private String productName;
    @Column(nullable=false) private Integer quantity;
    @Column(nullable=false, precision=12, scale=2) private BigDecimal total;
    @Column(name="order_date", nullable=false) private LocalDate orderDate;
    @Column(length=500) private String notes;
    @Column(nullable=false) private boolean ordered;
    @Column(nullable=false) private boolean prepared;
    @Column(nullable=false) private boolean paid;
    @Column(name="customer_username") private String customerUsername;
    @Column(name="payment_proof_url") private String paymentProofUrl;
    @Column(name="modification_count") private Integer modificationCount;
}
