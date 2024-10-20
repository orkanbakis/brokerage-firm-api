package com.brokeragefirm.application.port.output;


import com.brokeragefirm.domain.model.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface OrderPort {

  void addOrder(Order order);

  void updateOrder(Order order);

  Order getOrderByOrderId(UUID orderId);

  List<Order> getAllOrdersByCustomerId(UUID customerId, Pageable pageable);

  List<Order> getAllPendingOrders();
}
