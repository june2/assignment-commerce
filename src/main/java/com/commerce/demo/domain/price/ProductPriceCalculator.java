package com.commerce.demo.domain.price;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductPriceCalculator {
  
  /**
   * 상품 목록의 총 가격을 계산합니다. 도메인 로직: 여러 상품의 가격 합계 계산
   */
  public long calculateTotalPrice(List<ProductWithBrand> products) {
    return products.stream()
            .mapToLong(ProductWithBrand::getPrice)
            .sum();
  }
}
