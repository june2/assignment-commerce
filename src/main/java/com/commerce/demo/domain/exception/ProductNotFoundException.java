package com.commerce.demo.domain.exception;

/**
 * 상품을 찾을 수 없을 때 발생하는 예외
 */
public class ProductNotFoundException extends EntityNotFoundException {
    
    public ProductNotFoundException(Long productId) {
        super("상품", productId);
    }
    
    public ProductNotFoundException(String message) {
        super(message);
    }
} 