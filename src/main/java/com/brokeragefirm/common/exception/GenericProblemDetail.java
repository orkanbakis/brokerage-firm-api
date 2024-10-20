package com.brokeragefirm.common.exception;

import jakarta.annotation.Nullable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

@Getter
@EqualsAndHashCode(callSuper = true)
@Setter
public class GenericProblemDetail extends ProblemDetail {

  private int code;
  private List<ErrorSource> errors;

  public static GenericProblemDetail forStatusAndDetail(HttpStatusCode status, @Nullable String detail) {
    GenericProblemDetail problemDetail = new GenericProblemDetail();
    problemDetail.setStatus(status.value());
    problemDetail.setDetail(detail);
    return problemDetail;
  }
}

