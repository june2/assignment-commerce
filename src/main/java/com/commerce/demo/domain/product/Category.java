package com.commerce.demo.domain.product;

import java.util.Arrays;

/**
 * 상품 카테고리 enum 타입 안전성과 일관성을 보장하기 위해 enum으로 정의
 */
public enum Category {
  TOPS("상의"),
  OUTERWEAR("아우터"),
  PANTS("바지"),
  SNEAKERS("스니커즈"),
  BAGS("가방"),
  HATS("모자"),
  SOCKS("양말"),
  ACCESSORIES("액세서리");

  private final String displayName;

  Category(String displayName) {
    this.displayName = displayName;
  }

  /**
   * 표시명 반환
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * 표시명으로 Category enum 찾기
   */
  public static Category fromDisplayName(String displayName) {
    return Arrays.stream(values())
        .filter(category -> category.displayName.equals(displayName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 카테고리입니다: " + displayName));
  }

  /**
   * 카테고리 정렬 순서 반환 (0부터 시작)
   */
  public int getOrder() {
    return this.ordinal();
  }
} 
