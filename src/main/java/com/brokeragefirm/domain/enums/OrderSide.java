package com.brokeragefirm.domain.enums;

import lombok.Getter;

@Getter
public enum OrderSide {
  BUY("BUY"),
  SELL("SELL");

  private final String value;

  OrderSide(String value) {
    this.value = value;
  }
}
