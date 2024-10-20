package com.brokeragefirm.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brokeragefirm.application.port.output.OrderPort;
import com.brokeragefirm.domain.model.Order;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Pageable;

class OrderListingServiceTest {

  private OrderPort orderPort;

  @InjectMocks
  private OrderListingService orderListingService;

  @BeforeEach
  void setUp() {
    orderPort = mock(OrderPort.class);
    orderListingService = new OrderListingService(orderPort);
  }

  @Test
  void getAllOrdersByCustomerId_withValidCustomerId_returnsOrders() {
    UUID customerId = UUID.randomUUID();
    Pageable pageable = mock(Pageable.class);
    List<Order> expectedOrders = List.of(mock(Order.class));
    when(orderPort.getAllOrdersByCustomerId(customerId, pageable)).thenReturn(expectedOrders);
    List<Order> actualOrders = orderListingService.getAllOrdersByCustomerId(customerId, pageable);
    assertEquals(expectedOrders, actualOrders);
    verify(orderPort).getAllOrdersByCustomerId(customerId, pageable);
  }

  @Test
  void getAllOrdersByCustomerId_withValidCustomerId_returnsEmptyOrders() {
    UUID customerId = UUID.randomUUID();
    Pageable pageable = mock(Pageable.class);
    List<Order> expectedOrders = List.of();
    when(orderPort.getAllOrdersByCustomerId(customerId, pageable)).thenReturn(expectedOrders);
    List<Order> actualOrders = orderListingService.getAllOrdersByCustomerId(customerId, pageable);
    assertEquals(expectedOrders, actualOrders);
    verify(orderPort).getAllOrdersByCustomerId(customerId, pageable);
  }
}
