package com.brokeragefirm.common.config.message.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMQProperties {

  private String orderExchange;
  private String orderRoutingKey;
  private String orderQueue;
  private String orderDeadLetterExchange;
  private String orderDeadLetterRoutingKey;
  private String orderDeadLetterQueue;
  private int maxAttempts;
  private String connectionName;
  private int prefetchCount;
}
