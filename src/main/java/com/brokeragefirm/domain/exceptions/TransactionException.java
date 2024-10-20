package com.brokeragefirm.domain.exceptions;

import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.common.exception.GenericException;

public class TransactionException extends GenericException {

  public TransactionException(ErrorCode errorCode) {
    super(errorCode.getCode(), errorCode.getMessage());
  }
}
