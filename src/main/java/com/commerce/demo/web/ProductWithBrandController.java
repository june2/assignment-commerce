package com.commerce.demo.web;

import com.commerce.demo.application.ProductWithBrandService;
import com.commerce.demo.web.dto.CategoryLowestPriceAggregateResponse;
import com.commerce.demo.web.dto.BrandLowestPriceAggregateResponse;
import com.commerce.demo.web.dto.CategoryPriceRangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
public class ProductWithBrandController {

  private final ProductWithBrandService productWithBrandService;

  // 1. 카테고리별 최저가격 브랜드와 가격 조회 + 총액
  @GetMapping("/lowest-by-category")
  public ResponseEntity<CategoryLowestPriceAggregateResponse> findLowestPriceByCategory() {
    return ResponseEntity.ok(productWithBrandService.findLowestPriceByCategory());
  }

  // 2. 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격 브랜드와 상품가격 조회
  @GetMapping("/lowest-by-brand")
  public ResponseEntity<BrandLowestPriceAggregateResponse> findLowestTotalPriceBrand() {
    return ResponseEntity.ok(productWithBrandService.findLowestTotalPriceBrand());
  }

  // 3. 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회
  @GetMapping("/category/{category}/price-range")
  public ResponseEntity<CategoryPriceRangeResponse> findCategoryPriceRange(
      @PathVariable String category) {
    return ResponseEntity.ok(productWithBrandService.findCategoryPriceRange(category));
  }
} 
