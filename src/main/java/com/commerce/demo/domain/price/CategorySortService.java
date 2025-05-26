package com.commerce.demo.domain.price;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Comparator;

@Service
public class CategorySortService {

  // 카테고리 정렬 순서 정의
  private static final List<String> CATEGORY_ORDER = List.of(
      "상의", "아우터", "바지", "스니커즈", "가방", "모자", "양말", "액세서리"
  );

  /**
   * ProductWithBrand 리스트를 카테고리 순서에 따라 정렬
   */
  public List<ProductWithBrand> sortByCategory(List<ProductWithBrand> products) {
    return products.stream()
        .sorted(Comparator.comparing(product -> {
          int index = CATEGORY_ORDER.indexOf(product.getCategory());
          return index == -1 ? Integer.MAX_VALUE : index; // 정의되지 않은 카테고리는 마지막에 배치
        }))
        .toList();
  }

  /**
   * 카테고리 순서 인덱스 반환 (정렬 시 사용)
   */
  public int getCategoryOrder(String category) {
    int index = CATEGORY_ORDER.indexOf(category);
    return index == -1 ? Integer.MAX_VALUE : index;
  }
}