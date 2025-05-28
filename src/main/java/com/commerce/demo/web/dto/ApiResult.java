package com.commerce.demo.web.dto;

public record ApiResult<T>(boolean success, T data, String message) {
  
  public static <T> ApiResult<T> failure(String message) {
    return new ApiResult<>(false, null, message);
  }
} 
