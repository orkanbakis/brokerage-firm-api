package com.brokeragefirm.domain.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
  PENDING("PENDING"), MATCHED("MATCHED"), CANCELED("CANCELED");

  private final String value;

  OrderStatus(String value) {
    this.value = value;
  }
}
