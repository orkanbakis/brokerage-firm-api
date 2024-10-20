package com.brokeragefirm.common.exception;

public record ErrorSource(
    String field,
    int errorCode,
    String message
) {

}
