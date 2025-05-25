package com.commerce.demo.web.dto;

import com.commerce.demo.domain.brand.Brand;

public record BrandResponse(Long id, String name) {
  
  public static BrandResponse from(Brand brand) {
    return new BrandResponse(brand.getId(), brand.getName());
  }
} 