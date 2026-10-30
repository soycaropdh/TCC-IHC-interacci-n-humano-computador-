package com.igarrido.store.application.request;
import lombok.Data;
import java.math.BigDecimal;
@Data public class MaterialRequest { private String name; private String unit; private BigDecimal stock; private BigDecimal price; private BigDecimal batchConsumption; private BigDecimal minStock; }
