package com.brokeragefirm.common.config.message;

import com.brokeragefirm.common.config.message.properties.RabbitMQProperties;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.connection.SimplePropertyValueConnectionNameStrategy;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitMQConfig {

  private final RabbitMQProperties rabbitMQProperties;

  public RabbitMQConfig(RabbitMQProperties rabbitMQProperties) {
    this.rabbitMQProperties = rabbitMQProperties;
  }

  @Bean
  public DirectExchange orderExchange() {
    return new DirectExchange(rabbitMQProperties.getOrderExchange());
  }

  @Bean
  public DirectExchange orderDeadLetterExchange() {
    return new DirectExchange(rabbitMQProperties.getOrderDeadLetterExchange());
  }

  @Bean
  public Queue orderQueue() {
    return QueueBuilder.durable(rabbitMQProperties.getOrderQueue())
        .withArgument("x-dead-letter-exchange",
            rabbitMQProperties.getOrderDeadLetterExchange())
        .withArgument("x-dead-letter-routing-key",
            rabbitMQProperties.getOrderDeadLetterRoutingKey())
        .build();
  }

  @Bean
  public Queue orderDeadLetterQueue() {
    return QueueBuilder.durable(rabbitMQProperties.getOrderDeadLetterQueue())
        .withArgument("x-dead-letter-exchange",
            rabbitMQProperties.getOrderDeadLetterExchange())
        .withArgument("x-dead-letter-routing-key",
            rabbitMQProperties.getOrderDeadLetterRoutingKey())
        .build();
  }

  @Bean
  public Binding orderBinding() {
    return BindingBuilder.bind(orderQueue())
        .to(orderExchange())
        .with(rabbitMQProperties.getOrderRoutingKey());
  }

  @Bean
  public Binding orderDeadLetterBinding() {
    return BindingBuilder.bind(orderDeadLetterQueue())
        .to(orderDeadLetterExchange())
        .with(rabbitMQProperties.getOrderDeadLetterRoutingKey());
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
      RetryOperationsInterceptor retryOperationsInterceptor) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setAdviceChain(retryOperationsInterceptor);
    factory.setMessageConverter(jsonMessageConverter());
    factory.setPrefetchCount(rabbitMQProperties.getPrefetchCount());
    return factory;
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter(
        JsonMapper.builder().addModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build());
  }

  @Bean
  public RetryOperationsInterceptor retryOperationsInterceptor() {
    RetryTemplate retryTemplate = new RetryTemplate();
    retryTemplate.setRetryPolicy(new SimpleRetryPolicy(rabbitMQProperties.getMaxAttempts()));

    return RetryInterceptorBuilder.stateless().retryOperations(retryTemplate)
        .recoverer(new RejectAndDontRequeueRecoverer()).build();
  }

  @Bean
  public ConnectionNameStrategy connectionNameStrategy() {
    return new SimplePropertyValueConnectionNameStrategy(rabbitMQProperties.getConnectionName());
  }
}
