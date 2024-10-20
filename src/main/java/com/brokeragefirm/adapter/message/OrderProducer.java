package com.brokeragefirm.adapter.message;

import com.brokeragefirm.application.port.output.OrderPublisherPort;
import com.brokeragefirm.common.config.message.properties.RabbitMQProperties;
import com.brokeragefirm.domain.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderProducer implements OrderPublisherPort {

  private final RabbitMQProperties rabbitMQProperties;
  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publishOrder(Order order) {
    log.info("Publishing order to RabbitMQ: {} ", order);
    rabbitTemplate.convertAndSend(rabbitMQProperties.getOrderExchange(), rabbitMQProperties.getOrderRoutingKey(),
        order);
    log.info("Order published to RabbitMQ: {} ", order);
  }
}
