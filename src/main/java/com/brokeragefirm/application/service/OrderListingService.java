package com.brokeragefirm.application.service;

import com.brokeragefirm.application.port.input.OrderListingUseCase;
import com.brokeragefirm.application.port.output.OrderPort;
import com.brokeragefirm.domain.model.Order;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderListingService implements OrderListingUseCase {

  private final OrderPort orderPort;

  @Override
  public List<Order> getAllOrdersByCustomerId(UUID customerId, Pageable pageable) {
    return orderPort.getAllOrdersByCustomerId(customerId, pageable);
  }
}
