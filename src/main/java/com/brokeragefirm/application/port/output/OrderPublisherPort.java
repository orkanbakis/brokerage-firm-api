package com.brokeragefirm.application.port.output;

import com.brokeragefirm.domain.model.Order;

public interface OrderPublisherPort {

  void publishOrder(Order order);
}
