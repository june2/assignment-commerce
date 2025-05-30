package com.commerce.demo.web.exception;

import com.commerce.demo.application.exception.ApplicationException;
import com.commerce.demo.domain.exception.DomainException;
import com.commerce.demo.domain.exception.EntityNotFoundException;
import com.commerce.demo.domain.exception.InvalidValueException;
import com.commerce.demo.web.dto.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
  
  /**
   * Bean Validation 예외 처리 (400 Bad Request)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResult<Void>> handleValidationException(
          MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
    
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResult.failure("입력값 검증 실패: " + errorMessage));
  }
  
  /**
   * 엔티티를 찾을 수 없는 경우 (404 Not Found)
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiResult<Void>> handleEntityNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResult.failure(ex.getMessage()));
  }
  
  /**
   * 잘못된 값이 입력된 경우 (400 Bad Request)
   */
  @ExceptionHandler(InvalidValueException.class)
  public ResponseEntity<ApiResult<Void>> handleInvalidValue(InvalidValueException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResult.failure(ex.getMessage()));
  }
  
  /**
   * 기타 도메인 예외 (400 Bad Request)
   */
  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ApiResult<Void>> handleDomainException(DomainException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResult.failure(ex.getMessage()));
  }
  
  /**
   * 애플리케이션 계층 예외 (400 Bad Request)
   */
  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ApiResult<Void>> handleApplicationException(ApplicationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResult.failure(ex.getMessage()));
  }
  
  /**
   * IllegalArgumentException 처리 (하위 호환성을 위해 유지)
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResult<Void>> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResult.failure(ex.getMessage()));
  }
  
  /**
   * 예상하지 못한 예외 (500 Internal Server Error)
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResult<Void>> handleGeneral(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResult.failure("서버 내부 오류가 발생했습니다"));
  }
  
}
