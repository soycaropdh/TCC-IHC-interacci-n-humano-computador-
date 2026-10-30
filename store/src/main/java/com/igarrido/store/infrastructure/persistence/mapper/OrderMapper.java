package com.igarrido.store.infrastructure.persistence.mapper;
import com.igarrido.store.domain.model.Order;
import com.igarrido.store.infrastructure.persistence.entity.OrderEntity;
public class OrderMapper {
 public static Order toDomain(OrderEntity e){return Order.builder().id(e.getId()).customerName(e.getCustomerName()).phone(e.getPhone()).productId(e.getProductId()).productName(e.getProductName()).quantity(e.getQuantity()).total(e.getTotal()).orderDate(e.getOrderDate()).notes(e.getNotes()).ordered(e.isOrdered()).prepared(e.isPrepared()).paid(e.isPaid()).customerUsername(e.getCustomerUsername()).paymentProofUrl(e.getPaymentProofUrl()).modificationCount(e.getModificationCount()==null ? 0 : e.getModificationCount()).build();}
 public static OrderEntity toEntity(Order d){return OrderEntity.builder().id(d.getId()).customerName(d.getCustomerName()).phone(d.getPhone()).productId(d.getProductId()).productName(d.getProductName()).quantity(d.getQuantity()).total(d.getTotal()).orderDate(d.getOrderDate()).notes(d.getNotes()).ordered(d.isOrdered()).prepared(d.isPrepared()).paid(d.isPaid()).customerUsername(d.getCustomerUsername()).paymentProofUrl(d.getPaymentProofUrl()).modificationCount(d.getModificationCount()).build();}
}
