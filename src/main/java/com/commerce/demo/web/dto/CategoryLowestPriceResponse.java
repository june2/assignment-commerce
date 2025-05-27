package com.commerce.demo.web.dto;

import com.commerce.demo.domain.price.ProductWithBrand;

public record CategoryLowestPriceResponse(String category, String brandName, long price) {

  /**
   * ProductWithBrand 도메인 객체로부터 CategoryLowestPriceResponse DTO를 생성 web layer에서 response mapping 변환시
   * 사용
   */
  public static CategoryLowestPriceResponse from(ProductWithBrand product) {
    return new CategoryLowestPriceResponse(
        product.getCategory().getDisplayName(),
        product.getBrandName(),
        product.getPrice()
    );
  }
}
