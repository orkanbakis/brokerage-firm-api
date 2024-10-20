package com.brokeragefirm.adapter.persistence.repository;

import com.brokeragefirm.adapter.persistence.entity.OrderEntity;
import com.brokeragefirm.domain.enums.OrderStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {

  @Query("SELECT o FROM OrderEntity o WHERE o.id = :orderId")
  OrderEntity getOrderByOrderId(@Param("orderId") UUID orderId);

  @Query("SELECT o FROM OrderEntity o WHERE o.customerId = :customerId")
  Optional<List<OrderEntity>> findAllOrdersByCustomerId(@Param("customerId") UUID customerId, Pageable pageable);

  @Query("SELECT o FROM OrderEntity o WHERE o.status = :status")
  Optional<List<OrderEntity>> findAllOrdersByStatus(@Param("status") OrderStatus status);

}
