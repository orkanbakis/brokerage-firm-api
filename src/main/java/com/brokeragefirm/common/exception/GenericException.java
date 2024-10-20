package com.brokeragefirm.common.exception;

import lombok.Getter;

@Getter
public class GenericException extends RuntimeException {

  private final int errorCode;

  public GenericException(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
