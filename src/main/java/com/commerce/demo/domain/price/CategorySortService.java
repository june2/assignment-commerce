package com.commerce.demo.domain.price;

import com.commerce.demo.domain.product.Category;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Comparator;

@Service
public class CategorySortService {

  /**
   * ProductWithBrand 리스트를 카테고리 순서에 따라 정렬
   */
  public List<ProductWithBrand> sortByCategory(List<ProductWithBrand> products) {
    return products.stream()
        .sorted(Comparator.comparing(product -> product.getCategory().getOrder()))
        .toList();
  }
}
