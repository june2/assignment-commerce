package com.commerce.demo.web.exception;

import com.commerce.demo.web.dto.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResult<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.failure(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResult.failure(ex.getMessage()));
    }
    
    
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<ApiResult<Void>> handleNoHandlerFoundException(NoHandlerFoundException e) {
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(ApiResult.failure("요청한 리소스를 찾을 수 없습니다."));
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ApiResult<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(ApiResult.failure(e.getMessage()));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResult<Void>> handleException(Exception e) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ApiResult.failure("서버 내부 오류가 발생했습니다."));
//    }
} 