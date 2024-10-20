package com.brokeragefirm.adapter.persistence;

import com.brokeragefirm.adapter.persistence.entity.OrderEntity;
import com.brokeragefirm.adapter.persistence.repository.OrderRepository;
import com.brokeragefirm.application.mapper.OrderMapper;
import com.brokeragefirm.application.port.output.OrderPort;
import com.brokeragefirm.domain.enums.OrderStatus;
import com.brokeragefirm.domain.model.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OrderPostgresAdapter implements OrderPort {

  private final OrderMapper orderMapper;
  private final OrderRepository orderRepository;

  @Override
  public void addOrder(Order order) {
    log.info("Saving order with orderId {}", order.getId());
    orderRepository.save(orderMapper.toEntity(order));
    log.info("Order saved successfully with orderId {}", order.getId());
  }

  @Override
  public void updateOrder(Order order) {
    log.info("Updating order with orderId {}", order.getId());
    orderRepository.save(orderMapper.toEntity(order));
    log.info("Order updated successfully with orderId {}", order.getId());
  }

  @Override
  public Order getOrderByOrderId(UUID orderId) {
    log.info("Getting order with orderId {}", orderId);
    return orderMapper.toModel(orderRepository.getOrderByOrderId(orderId));
  }

  @Override
  public List<Order> getAllOrdersByCustomerId(UUID customerId, Pageable pageable) {
    log.info("Getting all orders by customerId {}", customerId);
    Optional<List<OrderEntity>> orders = orderRepository.findAllOrdersByCustomerId(customerId, pageable);
    return orders.flatMap(
            orderEntities -> Optional.of(orderEntities.stream().map(orderMapper::toModel).collect(Collectors.toList())))
        .orElse(List.of());
  }

  @Override
  public List<Order> getAllPendingOrders() {
    log.info("Getting all pending orders");
    Optional<List<OrderEntity>> orders = orderRepository.findAllOrdersByStatus(OrderStatus.PENDING);
    return orders.flatMap(
            orderEntities -> Optional.of(orderEntities.stream().map(orderMapper::toModel).collect(Collectors.toList())))
        .orElse(List.of());
  }


}
