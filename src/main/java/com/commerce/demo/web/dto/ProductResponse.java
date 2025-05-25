package com.commerce.demo.web.dto;

import com.commerce.demo.domain.product.Product;

public record ProductResponse(Long id, String name, String category, long price, Long brandId, String brandName) {
  
  public static ProductResponse from(Product product) {
    return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getCategory(),
            product.getPrice().getValue(),
            product.getBrand() != null ? product.getBrand().getId() : null,
            product.getBrand() != null ? product.getBrand().getName() : null
    );
  }
} 