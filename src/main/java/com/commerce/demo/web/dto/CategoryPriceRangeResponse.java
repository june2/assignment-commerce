package com.commerce.demo.web.dto;

import com.commerce.demo.domain.price.ProductWithBrand;
import java.util.List;

public record CategoryPriceRangeResponse(
    String 카테고리,
    List<BrandPriceResponse> 최저가,
    List<BrandPriceResponse> 최고가
) {

  /**
   * 카테고리명과 최저가/최고가 상품들로부터 CategoryPriceRangeResponse DTO를 생성 web layer에서 response mapping 변환시 사용
   */
  public static CategoryPriceRangeResponse from(String category,
      List<ProductWithBrand> lowestProducts, List<ProductWithBrand> highestProducts) {
    List<BrandPriceResponse> 최저가 = lowestProducts.stream()
        .map(BrandPriceResponse::from)
        .toList();

    List<BrandPriceResponse> 최고가 = highestProducts.stream()
        .map(BrandPriceResponse::from)
        .toList();

    return new CategoryPriceRangeResponse(category, 최저가, 최고가);
  }
}
