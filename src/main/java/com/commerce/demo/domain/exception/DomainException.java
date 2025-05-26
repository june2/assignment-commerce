package com.commerce.demo.domain.exception;

/**
 * 도메인 계층의 기본 예외 클래스
 * 모든 도메인 예외는 이 클래스를 상속받아야 함
 */
public abstract class DomainException extends RuntimeException {
    
    protected DomainException(String message) {
        super(message);
    }
    
    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
} 