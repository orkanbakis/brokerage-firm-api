package com.brokeragefirm.common.constants;

import lombok.Getter;

@Getter
public enum ErrorCode {
  BAD_REQUEST(10, "Bad Request"),
  UNAUTHORIZED(11, "Unauthorized"),
  FORBIDDEN(12, "Forbidden"),
  METHOD_NOT_ALLOWED(13, "Method Not Allowed"),
  CONFLICT(14, "Conflict"),
  INTERNAL_SERVER_ERROR(15, "Internal Server Error"),
  SERVICE_UNAVAILABLE(16, "Service Unavailable"),

  VALIDATION_ERROR(101, "Validation Error"),
  NOT_AVAILABLE(102, "Not Available Error"),
  INVALID_REQUEST(103, "Invalid Request"),

  //Transaction Exceptions
  TRANSACTION_NOT_FOUND(200, "Transaction Not Found"),
  //Asset Exceptions
  INSUFFICIENT_BALANCE(201, "Insufficient Balance"),
  ASSET_NOT_FOUND(202, "Asset Not Found"),
  ASSET_MUST_BE_ABOVE_ZERO(203, "must be greater than 0"),
  INSUFFIENCT_ASSET(204, "Insufficient Asset"),

  //Order Exceptions
  ORDER_NOT_FOUND(300, "Order Not Found"),
  INVALID_ORDER_STATUS(301, "Invalid Order Status"),
  NO_ORDER_FOUND_TO_PROCESS(302, "No order found to process."),
  CURRENCY_NOT_SUPPORTED(303, "Currency not supported"),

  //Price Exception
  PRICE_MUST_BE_ABOVE_ZERO(400, "Price must be greater than 0");

  private final int code;

  private final String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

}
