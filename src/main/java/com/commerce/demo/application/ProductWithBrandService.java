package com.commerce.demo.application;

import com.commerce.demo.domain.price.ProductPriceCalculator;
import com.commerce.demo.domain.price.ProductWithBrand;
import com.commerce.demo.domain.price.ProductWithBrandRepository;
import com.commerce.demo.domain.price.CategorySortService;
import com.commerce.demo.web.dto.CategoryLowestPriceAggregateResponse;
import com.commerce.demo.web.dto.BrandLowestPriceAggregateResponse;
import com.commerce.demo.web.dto.CategoryPriceRangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductWithBrandService {

  private final ProductWithBrandRepository productWithBrandRepository;
  private final CategorySortService categorySortService;

  /**
   * 카테고리별 최저가 상품 조회 Use Case: 각 카테고리에서 가장 저렴한 상품들을 조회하고 총액과 함께 반환
   */
  public CategoryLowestPriceAggregateResponse findLowestPriceByCategory() {
    ProductPriceCalculator productPriceCalculator = new ProductPriceCalculator();
    // 카테고리별 최저가 상품 조회
    List<ProductWithBrand> lowestProducts = productWithBrandRepository.findLowestPriceProductsByCategory();
    // 카테고리 순서에 따라 정렬
    List<ProductWithBrand> sortedProducts = categorySortService.sortByCategory(lowestProducts);
    // 총 가격 계산
    long totalPrice = productPriceCalculator.calculateTotalPrice(sortedProducts);

    return CategoryLowestPriceAggregateResponse.from(sortedProducts, totalPrice);
  }

  /**
   * 단일 브랜드 최저가 상품 조회 Use Case: 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 상품들을 조회
   */
  public BrandLowestPriceAggregateResponse findLowestTotalPriceBrand() {
    ProductPriceCalculator productPriceCalculator = new ProductPriceCalculator();
    // 단일 브랜드로 모든 카테고리 상품 구매 시 최저가 브랜드의 상품들 조회
    List<ProductWithBrand> lowestBrandProducts = productWithBrandRepository.findLowestTotalPriceBrandProducts();

    if (lowestBrandProducts.isEmpty()) {
      throw new IllegalStateException("상품 데이터가 존재하지 않습니다.");
    }

    // 카테고리 순서에 따라 정렬
    List<ProductWithBrand> sortedProducts = categorySortService.sortByCategory(lowestBrandProducts);
    // 총 가격 계산
    long totalPrice = productPriceCalculator.calculateTotalPrice(sortedProducts);

    return BrandLowestPriceAggregateResponse.from(sortedProducts, totalPrice);
  }

  /**
   * 카테고리별 최저가, 최고가 브랜드 조회 Use Case: 특정 카테고리에서 최저가와 최고가 브랜드의 상품 가격을 조회
   */
  public CategoryPriceRangeResponse findCategoryPriceRange(String category) {
    // 해당 카테고리의 최저가 상품들 조회
    List<ProductWithBrand> lowestProducts = productWithBrandRepository.findLowestPriceProductsInCategory(
        category);

    // 해당 카테고리의 최고가 상품들 조회
    List<ProductWithBrand> highestProducts = productWithBrandRepository.findHighestPriceProductsInCategory(
        category);

    if (lowestProducts.isEmpty() || highestProducts.isEmpty()) {
      throw new IllegalStateException("해당 카테고리의 상품 데이터가 존재하지 않습니다: " + category);
    }

    return CategoryPriceRangeResponse.from(category, lowestProducts, highestProducts);
  }
}
