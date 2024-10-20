package com.brokeragefirm.common.advice;


import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.common.exception.GenericProblemDetail;
import com.brokeragefirm.domain.exceptions.AssetException;
import com.brokeragefirm.domain.exceptions.OrderException;
import com.brokeragefirm.domain.exceptions.TransactionException;
import jakarta.persistence.OptimisticLockException;
import java.io.InvalidClassException;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private static final String NOT_FOUND_URL = "https://httpstatuses.com/404";
  private static final String BAD_REQUEST_URL = "https://httpstatuses.com/400";

  @ExceptionHandler(TransactionException.class)
  public ResponseEntity<GenericProblemDetail> handleTransactionException(TransactionException e) {
    log.warn("handleTransactionException ex: {}", e.getMessage());
    GenericProblemDetail problemDetail = GenericProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    problemDetail.setType(URI.create(NOT_FOUND_URL));
    problemDetail.setTitle("Transaction Exception");
    problemDetail.setCode(ErrorCode.FORBIDDEN.getCode());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
  }

  @ExceptionHandler(AssetException.class)
  public ResponseEntity<GenericProblemDetail> handleAssetException(AssetException e) {
    log.warn("handleAssetException ex: {}", e.getMessage());
    GenericProblemDetail problemDetail = GenericProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    problemDetail.setType(URI.create(NOT_FOUND_URL));
    problemDetail.setTitle("Asset Exception");
    problemDetail.setCode(ErrorCode.FORBIDDEN.getCode());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
  }

  @ExceptionHandler(OrderException.class)
  public ResponseEntity<GenericProblemDetail> handleOrderException(OrderException e) {
    log.warn("handleOrderException ex: {}", e.getMessage());
    GenericProblemDetail problemDetail = GenericProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    problemDetail.setType(URI.create(NOT_FOUND_URL));
    problemDetail.setTitle("Order Exception");
    problemDetail.setCode(ErrorCode.FORBIDDEN.getCode());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
  }

  @ExceptionHandler(InvalidClassException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ProblemDetail> handleInvalidClassException(InvalidClassException ex) {
    log.warn("handleInvalidClassException ex: {}", ex.getMessage());
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setType(URI.create(BAD_REQUEST_URL));
    problemDetail.setTitle("Invalid Class Exception");
    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    log.warn("handleMethodArgumentTypeMismatchException ex: {}", ex.getMessage());
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setType(URI.create(BAD_REQUEST_URL));
    problemDetail.setTitle("Method Argument Type Mismatch Exception");
    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ProblemDetail> handleNullPointerException(NullPointerException ex) {
    log.warn("handleNullPointerException ex: {}", ex.getMessage());
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setType(URI.create(BAD_REQUEST_URL));
    problemDetail.setTitle("Null Pointer Exception");
    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(OptimisticLockException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ProblemDetail> handleOptimisticLockException(OptimisticLockException ex) {
    log.warn("handleOptimisticLockException ex: {}", ex.getMessage());
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setType(URI.create(BAD_REQUEST_URL));
    problemDetail.setTitle("Null Pointer Exception");
    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }
}
