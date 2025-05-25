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
    ProductWithBrand product1 = new ProductWithBrand(1L, "상의", "티셔츠", 10000L, 1L, "A");
    ProductWithBrand product2 = new ProductWithBrand(2L, "하의", "바지", 15000L, 1L, "A");
    ProductWithBrand product3 = new ProductWithBrand(3L, "신발", "운동화", 20000L, 2L, "B");

    List<ProductWithBrand> products = Arrays.asList(product1, product2, product3);

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
    ProductWithBrand product = new ProductWithBrand(1L, "상의", "티셔츠", 12500L, 1L, "A");
    List<ProductWithBrand> products = List.of(product);

    // when
    long totalPrice = productPriceCalculator.calculateTotalPrice(products);

    // then
    assertThat(totalPrice).isEqualTo(12500);
  }

  @Test
  @DisplayName("같은 가격의 여러 상품들의 총 가격을 올바르게 계산한다")
  void calculateTotalPrice_samePriceProducts() {
    // given
    ProductWithBrand product1 = new ProductWithBrand(1L, "상의", "티셔츠", 10000L, 1L, "A");
    ProductWithBrand product2 = new ProductWithBrand(2L, "하의", "바지", 10000L, 1L, "A");
    ProductWithBrand product3 = new ProductWithBrand(3L, "신발", "운동화", 10000L, 1L, "A");

    List<ProductWithBrand> products = Arrays.asList(product1, product2, product3);

    // when
    long totalPrice = productPriceCalculator.calculateTotalPrice(products);

    // then
    assertThat(totalPrice).isEqualTo(30000);
  }

  @Test
  @DisplayName("0원 상품이 포함된 목록의 총 가격을 올바르게 계산한다")
  void calculateTotalPrice_withZeroPriceProduct() {
    // given
    ProductWithBrand freeProduct = new ProductWithBrand(1L, "증정품", "무료샘플", 0L, 1L, "A");
    ProductWithBrand paidProduct = new ProductWithBrand(2L, "상의", "티셔츠", 15000L, 1L, "A");

    List<ProductWithBrand> products = Arrays.asList(freeProduct, paidProduct);

    // when
    long totalPrice = productPriceCalculator.calculateTotalPrice(products);

    // then
    assertThat(totalPrice).isEqualTo(15000);
  }
} 
