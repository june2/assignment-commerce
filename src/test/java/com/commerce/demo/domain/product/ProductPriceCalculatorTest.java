package com.commerce.demo.domain.product;

import com.commerce.demo.domain.price.ProductPriceCalculator;
import com.commerce.demo.domain.price.ProductWithBrand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductPriceCalculatorTest {

  private final ProductPriceCalculator productPriceCalculator = new ProductPriceCalculator();

  @Test
  @DisplayName("상품 목록의 총 가격을 올바르게 계산한다")
  void calculateTotalPrice_validProducts() {
    // given
    List<ProductWithBrand> products = List.of(
        new ProductWithBrand(1L, Category.TOPS, "티셔츠", 10000L, 1L, "A"),
        new ProductWithBrand(2L, Category.PANTS, "바지", 15000L, 1L, "A"),
        new ProductWithBrand(3L, Category.SNEAKERS, "운동화", 20000L, 2L, "B")
    );

    // when
    long totalPrice = productPriceCalculator.calculateTotalPrice(products);

    // then
    assertThat(totalPrice).isEqualTo(45000);
  }

  @Test
  @DisplayName("빈 상품 목록의 총 가격은 0이다")
  void calculateTotalPrice_emptyList() {
    // given
    List<ProductWithBrand> emptyProducts = Collections.emptyList();

    // when
    long totalPrice = productPriceCalculator.calculateTotalPrice(emptyProducts);

    // then
    assertThat(totalPrice).isEqualTo(0);
  }

  @Test
  @DisplayName("단일 상품의 총 가격을 올바르게 계산한다")
  void calculateTotalPrice_singleProduct() {
    // given
    List<ProductWithBrand> products = List.of(
        new ProductWithBrand(1L, Category.TOPS, "티셔츠", 12500L, 1L, "A")
    );

    // when
    long totalPrice = productPriceCalculator.calculateTotalPrice(products);

    // then
    assertThat(totalPrice).isEqualTo(12500);
  }

  @Test
  @DisplayName("같은 가격의 여러 상품들의 총 가격을 올바르게 계산한다")
  void calculateTotalPrice_samePriceProducts() {
    // given
    List<ProductWithBrand> products = List.of(
        new ProductWithBrand(1L, Category.TOPS, "티셔츠", 10000L, 1L, "A"),
        new ProductWithBrand(2L, Category.PANTS, "바지", 10000L, 1L, "A"),
        new ProductWithBrand(3L, Category.SNEAKERS, "운동화", 10000L, 1L, "A")
    );

    // when
    long totalPrice = productPriceCalculator.calculateTotalPrice(products);

    // then
    assertThat(totalPrice).isEqualTo(30000);
  }

  @Test
  @DisplayName("0원 상품이 포함된 목록의 총 가격을 올바르게 계산한다")
  void calculateTotalPrice_withZeroPriceProduct() {
    // given
    List<ProductWithBrand> products = List.of(
        new ProductWithBrand(1L, Category.ACCESSORIES, "무료샘플", 0L, 1L, "A"),
        new ProductWithBrand(2L, Category.TOPS, "티셔츠", 15000L, 1L, "A")
    );

    // when
    long totalPrice = productPriceCalculator.calculateTotalPrice(products);

    // then
    assertThat(totalPrice).isEqualTo(15000);
  }
} 
