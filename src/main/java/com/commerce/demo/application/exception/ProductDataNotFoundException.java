package com.commerce.demo.application.exception;

/**
 * 상품 데이터가 존재하지 않을 때 발생하는 예외
 */
public class ProductDataNotFoundException extends ApplicationException {
    
    public ProductDataNotFoundException() {
        super("상품 데이터가 존재하지 않습니다.");
    }
    
    public ProductDataNotFoundException(String message) {
        super(message);
    }
    
    public static ProductDataNotFoundException forCategory(String category) {
        return new ProductDataNotFoundException("해당 카테고리의 상품 데이터가 존재하지 않습니다: " + category);
    }
} 