package com.commerce.demo.web.dto;

import com.commerce.demo.domain.price.ProductWithBrand;

public record BrandCategoryPriceResponse(String 카테고리, String 가격) {

  /**
   * ProductWithBrand 도메인 객체로부터 BrandCategoryPriceResponse DTO를 생성 web layer에서 response mapping 변환시
   * 사용
   */
  public static BrandCategoryPriceResponse from(ProductWithBrand product) {
    return new BrandCategoryPriceResponse(
        product.getCategory(),
        formatPrice(product.getPrice())
    );
  }

  private static String formatPrice(long price) {
    return String.format("%,d", price);
  }
}
