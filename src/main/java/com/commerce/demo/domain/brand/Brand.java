package com.commerce.demo.domain.brand;

import com.commerce.demo.infrastructure.brand.BrandJpaEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Brand {

  private final Long id;
  private final String name;

  public static Brand from(BrandJpaEntity entity) {
    return new Brand(entity.getId(), entity.getName());
  }
} 
