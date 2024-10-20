package com.brokeragefirm.application.port.input;


import com.brokeragefirm.domain.model.Order;

public interface OrderProcessingUseCase {

  void processOrder(Order order);
}
