package com.brokeragefirm.domain.model;

import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.domain.enums.OrderSide;
import com.brokeragefirm.domain.enums.OrderStatus;
import com.brokeragefirm.domain.exceptions.OrderException;
import com.brokeragefirm.domain.valueobjects.Price;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Order implements Serializable {

  private String id;
  private UUID customerId;
  private String assetName;
  private OrderSide orderSide;
  private Price price;
  private double size;
  private OrderStatus status;
  private Instant createdAt;

  public Order(UUID customerId, String assetName, OrderSide orderSide, Price price, int size) {
    this.id = UUID.randomUUID().toString();
    this.customerId = customerId;
    this.assetName = assetName;
    this.orderSide = orderSide;
    this.price = price;
    this.size = size;
    this.status = OrderStatus.PENDING;
    this.createdAt = Instant.now();
  }

  public void checkIfOrderExists(Order order) {
    if (order == null) {
      throw new OrderException(ErrorCode.ORDER_NOT_FOUND);
    }
  }
}
