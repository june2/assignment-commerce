package com.commerce.demo.web.dto;

import com.commerce.demo.domain.price.ProductWithBrand;

public record BrandPriceResponse(String 브랜드, String 가격) {

  /**
   * ProductWithBrand 도메인 객체로부터 BrandPriceResponse DTO를 생성 web layer에서 response mapping 변환시 사용
   */
  public static BrandPriceResponse from(ProductWithBrand product) {
    return new BrandPriceResponse(
        product.getBrandName(),
        formatPrice(product.getPrice())
    );
  }

  private static String formatPrice(long price) {
    return String.format("%,d", price);
  }
}
