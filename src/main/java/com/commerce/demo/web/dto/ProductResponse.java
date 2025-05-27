package com.commerce.demo.web.dto;

import com.commerce.demo.domain.product.Product;
import com.commerce.demo.domain.product.Category;

public record ProductResponse(Long id, String name, String category, long price, Long brandId, String brandName) {
  
  public static ProductResponse from(Product product) {
    return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getCategory().getDisplayName(),
            product.getPrice().getValue(),
            product.getBrand() != null ? product.getBrand().getId() : null,
            product.getBrand() != null ? product.getBrand().getName() : null
    );
  }
} 