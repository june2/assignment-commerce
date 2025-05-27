package com.commerce.demo.web;

import com.commerce.demo.application.ProductWithBrandService;
import com.commerce.demo.domain.product.Category;
import com.commerce.demo.web.dto.CategoryLowestPriceAggregateResponse;
import com.commerce.demo.web.dto.BrandLowestPriceAggregateResponse;
import com.commerce.demo.web.dto.CategoryPriceRangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
public class ProductWithBrandController {

  private final ProductWithBrandService productWithBrandService;

  // 카테고리 enum 값들 조회
  @GetMapping("/categories")
  public ResponseEntity<List<Map<String, String>>> getCategories() {
    List<Map<String, String>> categories = Arrays.stream(Category.values())
        .map(category -> Map.of(
            "value", category.name(),
            "label", category.getDisplayName()
        ))
        .collect(Collectors.toList());
    return ResponseEntity.ok(categories);
  }

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
  @GetMapping("/category/{categoryDisplayName}/price-range")
  public ResponseEntity<CategoryPriceRangeResponse> findCategoryPriceRange(
      @PathVariable String categoryDisplayName) {
    Category category = Category.fromDisplayName(categoryDisplayName);
    return ResponseEntity.ok(productWithBrandService.findCategoryPriceRange(category));
  }
} 
