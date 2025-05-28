package com.commerce.demo.domain.exception;

/**
 * 잘못된 값이 입력되었을 때 발생하는 예외
 */
public class InvalidValueException extends DomainException {
  
  public InvalidValueException(String message) {
    super(message);
  }
  
  public InvalidValueException(String fieldName, Object value) {
    super(String.format("잘못된 %s 값입니다: %s", fieldName, value));
  }
}
