package com.commerce.demo.domain.exception;

/**
 * 브랜드를 찾을 수 없을 때 발생하는 예외
 */
public class BrandNotFoundException extends EntityNotFoundException {
  
  public BrandNotFoundException(Long brandId) {
    super("브랜드", brandId);
  }
}
