package io.github.sambouch79.demo.shared.exception;

import lombok.Getter;

/** Exception métier avec code HTTP personnalisé. */
@Getter
public class BusinessException extends RuntimeException {

  private final int httpStatus;

  public BusinessException(String message) {
    super(message);
    this.httpStatus = 400;
  }

  public BusinessException(String message, int httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }
}
