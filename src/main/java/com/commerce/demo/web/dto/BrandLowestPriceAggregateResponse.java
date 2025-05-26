package com.commerce.demo.web.dto;

import com.commerce.demo.domain.price.ProductWithBrand;
import java.util.List;

public record BrandLowestPriceAggregateResponse(
    BrandLowestPriceDetail 최저가,
    String 총액
) {

  public static BrandLowestPriceAggregateResponse from(List<ProductWithBrand> products,
      long totalPrice) {
    List<BrandCategoryPriceResponse> categoryList = products.stream()
        .map(BrandCategoryPriceResponse::from)
        .toList();

    // 브랜드명 추출 (모든 상품이 같은 브랜드이므로 첫 번째 상품의 브랜드명 사용)
    String brandName = products.get(0).getBrandName();
    BrandLowestPriceDetail 최저가 = new BrandLowestPriceDetail(brandName, categoryList);
    return new BrandLowestPriceAggregateResponse(최저가, formatPrice(totalPrice));
  }

  private static String formatPrice(long price) {
    return String.format("%,d", price);
  }

  public record BrandLowestPriceDetail(
      String 브랜드,
      List<BrandCategoryPriceResponse> 카테고리
  ) {

  }
}
