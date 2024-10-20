package com.brokeragefirm.domain.enums;

import lombok.Getter;

@Getter
public enum Currency {
  TRY("TRY");

  private final String value;

  Currency(String value) {
    this.value = value;
  }
}
