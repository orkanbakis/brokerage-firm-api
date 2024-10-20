package com.brokeragefirm.domain.enums;

import lombok.Getter;

@Getter
public enum AssetType {
  STOCK("STOCK"),
  CURRENCY("CURRENCY");

  private final String value;

  AssetType(String value) {
    this.value = value;
  }
}
