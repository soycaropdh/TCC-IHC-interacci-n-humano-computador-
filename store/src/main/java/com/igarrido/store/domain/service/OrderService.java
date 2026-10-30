package com.igarrido.store.domain.service;

import com.igarrido.store.domain.model.Order;
import com.igarrido.store.domain.model.Product;
import com.igarrido.store.domain.repository.ProductRepository;
import com.igarrido.store.infrastructure.persistence.entity.OrderEntity;
import com.igarrido.store.infrastructure.persistence.jpa.OrderJpaRepository;
import com.igarrido.store.infrastructure.persistence.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
 private final OrderJpaRepository repo; private final ProductRepository products;
 public OrderService(OrderJpaRepository repo, ProductRepository products){this.repo=repo;this.products=products;}
 public List<Order> findAll(){return repo.findAllByOrderByOrderDateDescIdDesc().stream().map(OrderMapper::toDomain).collect(Collectors.toList());}
 public List<Order> findAllByCustomer(String username){return repo.findAllByCustomerUsernameOrderByOrderDateDescIdDesc(username).stream().map(OrderMapper::toDomain).collect(Collectors.toList());}
 public Optional<Order> findById(Long id){return repo.findById(id).map(OrderMapper::toDomain);}
 public Order create(String customerName,String phone,Long productId,Integer quantity,String notes,String username){
  if(customerName==null||customerName.trim().isEmpty()) throw new IllegalArgumentException("El nombre del cliente es obligatorio");
  if(quantity==null||quantity<1) throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
  Product p=products.findById(productId).orElseThrow(()->new IllegalArgumentException("El producto no existe"));
  BigDecimal total=p.getPrice().multiply(BigDecimal.valueOf(quantity));
  Order o=Order.builder().customerName(customerName.trim()).phone(phone).productId(productId).productName(p.getName()).quantity(quantity).total(total).orderDate(LocalDate.now()).notes(notes).ordered(true).prepared(false).paid(false).customerUsername(username).modificationCount(0).build();
  return OrderMapper.toDomain(repo.save(OrderMapper.toEntity(o)));
 }
 public Order updateStatus(Long id, boolean ordered, boolean prepared, boolean paid){
  OrderEntity e=repo.findById(id).orElseThrow(()->new IllegalArgumentException("El encargo no existe"));
  e.setOrdered(ordered); e.setPrepared(prepared); e.setPaid(paid); return OrderMapper.toDomain(repo.save(e));
 }
 public Order update(Long id,String username,String customerName,String phone,Integer quantity,String notes){
  OrderEntity e=owned(id,username);
  if((e.getModificationCount()==null?0:e.getModificationCount())>=2) throw new IllegalArgumentException("Ya utilizaste las 2 modificaciones permitidas para este encargo");
  if(customerName==null||customerName.trim().isEmpty()) throw new IllegalArgumentException("El nombre del cliente es obligatorio");
  if(quantity==null||quantity<1) throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
  Product p=products.findById(e.getProductId()).orElseThrow(()->new IllegalArgumentException("El producto no existe"));
  e.setCustomerName(customerName.trim()); e.setPhone(phone); e.setQuantity(quantity); e.setNotes(notes); e.setTotal(p.getPrice().multiply(BigDecimal.valueOf(quantity))); e.setModificationCount((e.getModificationCount()==null?0:e.getModificationCount())+1);
  return OrderMapper.toDomain(repo.save(e));
 }
 public Order markPaid(Long id,String username,String proofUrl){
  OrderEntity e=owned(id,username);
  if(e.getPaymentProofUrl()!=null && !e.getPaymentProofUrl().isBlank()) throw new IllegalArgumentException("El comprobante de pago ya fue registrado");
  e.setPaymentProofUrl(proofUrl); e.setPaid(true); return OrderMapper.toDomain(repo.save(e));
 }
 public void deleteAdmin(Long id){ repo.deleteById(id); }
 public void delete(Long id,String username){
  OrderEntity e=owned(id,username);
  if((e.getModificationCount()==null?0:e.getModificationCount())>=2) throw new IllegalArgumentException("Ya utilizaste las 2 modificaciones permitidas para este encargo");
  repo.delete(e);
 }
 private OrderEntity owned(Long id,String username){
  OrderEntity e=repo.findById(id).orElseThrow(()->new IllegalArgumentException("El encargo no existe"));
  if(e.getCustomerUsername()==null || !e.getCustomerUsername().equals(username)) throw new IllegalArgumentException("No puedes modificar este encargo");
  return e;
 }
}
