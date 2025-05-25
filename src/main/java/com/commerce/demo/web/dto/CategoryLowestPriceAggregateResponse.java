package com.commerce.demo.web.dto;

import com.commerce.demo.domain.price.ProductWithBrand;
import java.util.List;

public record CategoryLowestPriceAggregateResponse(
    List<CategoryLowestPriceResponse> items,
    long total
) {

  /**
   * ProductWithBrand 리스트와 총가격으로부터 CategoryLowestPriceAggregateResponse DTO를 생성 web layer에서 response
   * mapping 변환시 사용
   */
  public static CategoryLowestPriceAggregateResponse from(List<ProductWithBrand> products,
      long totalPrice) {
    List<CategoryLowestPriceResponse> items = products.stream()
        .map(CategoryLowestPriceResponse::from)
        .toList();
    return new CategoryLowestPriceAggregateResponse(items, totalPrice);
  }
}
