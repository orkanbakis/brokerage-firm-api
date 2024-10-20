package com.brokeragefirm.domain.exceptions;

import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.common.exception.GenericException;

public class OrderException extends GenericException {

  public OrderException(ErrorCode errorCode) {
    super(errorCode.getCode(), errorCode.getMessage());
  }
}
