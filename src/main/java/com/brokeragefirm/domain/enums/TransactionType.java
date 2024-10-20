package com.brokeragefirm.domain.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
  DEPOSIT("DEPOSIT"),
  WITHDRAW("WITHDRAW");

  private final String value;

  TransactionType(String value) {
    this.value = value;
  }
}
