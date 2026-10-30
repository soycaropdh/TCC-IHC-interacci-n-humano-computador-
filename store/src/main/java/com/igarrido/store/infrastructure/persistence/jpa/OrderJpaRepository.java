package com.igarrido.store.infrastructure.persistence.jpa;
import com.igarrido.store.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
 List<OrderEntity> findAllByOrderByOrderDateDescIdDesc();
 List<OrderEntity> findAllByCustomerUsernameOrderByOrderDateDescIdDesc(String customerUsername);
}
