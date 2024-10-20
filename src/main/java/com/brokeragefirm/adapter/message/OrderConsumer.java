package com.brokeragefirm.adapter.message;

import com.brokeragefirm.application.port.input.OrderProcessingUseCase;
import com.brokeragefirm.domain.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

  private final OrderProcessingUseCase orderProcessingUseCase;

  @RabbitListener(queues = "brokerage-firm-order-queue")
  public void consumeOrder(Order order) {
    log.info("Received order message from RabbitMQ. Processing Order: {}", order);
    orderProcessingUseCase.processOrder(order);
  }
}
