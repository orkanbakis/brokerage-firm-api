package com.brokeragefirm.domain.exceptions;

import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.common.exception.GenericException;

public class AssetException extends GenericException {

  public AssetException(ErrorCode errorCode) {
    super(errorCode.getCode(), errorCode.getMessage());
  }

  public AssetException(ErrorCode errorCode, String message) {
    super(errorCode.getCode(), message);
  }
}
