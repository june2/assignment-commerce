package com.commerce.demo.domain.product;

import com.commerce.demo.domain.brand.Brand;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Product {

  private final Long id;
  private final String category;
  private final String name;
  private final Money price;
  private final Brand brand;

  // 새로운 상품 생성을 위한 팩토리 메소드 (ID 없음)
  public static Product create(String category, String name, Money price, Brand brand) {
    return new Product(null, category, name, price, brand);
  }

  // 기존 상품 재구성을 위한 팩토리 메소드 (ID 있음)
  public static Product of(Long id, String category, String name, Money price, Brand brand) {
    return new Product(id, category, name, price, brand);
  }

  // 새로운 상품인지 확인
  public boolean isNew() {
    return this.id == null;
  }
} 
