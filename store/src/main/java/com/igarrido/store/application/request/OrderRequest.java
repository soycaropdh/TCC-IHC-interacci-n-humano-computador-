package com.igarrido.store.application.request;
import lombok.Data;
@Data public class OrderRequest { private String customerName; private String phone; private Long productId; private Integer quantity; private String notes; }
