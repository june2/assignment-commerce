package com.commerce.demo.application.exception;

/**
 * 애플리케이션 계층의 기본 예외 클래스 모든 애플리케이션 예외는 이 클래스를 상속받아야 함
 */
public abstract class ApplicationException extends RuntimeException {
  
  protected ApplicationException(String message) {
    super(message);
  }
  
} 