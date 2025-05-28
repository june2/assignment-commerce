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
  
  // 새로운 브랜드 생성을 위한 팩토리 메소드 (ID 없음)
  public static Brand create(String name) {
    return new Brand(null, name);
  }
  
  // 기존 브랜드 재구성을 위한 팩토리 메소드 (ID 있음)
  public static Brand of(Long id, String name) {
    return new Brand(id, name);
  }
  
  // ID만으로 브랜드 참조 생성 (이름은 나중에 로드)
  public static Brand reference(Long id) {
    return new Brand(id, null);
  }
  
  public static Brand from(BrandJpaEntity entity) {
    return new Brand(entity.getId(), entity.getName());
  }
} 
