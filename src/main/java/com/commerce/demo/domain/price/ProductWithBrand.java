package com.commerce.demo.domain.price;

import com.commerce.demo.domain.product.Category;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ProductWithBrand {

  private Long id;
  private Category category;
  private String name;
  private Long price;
  private Long brandId;
  private String brandName;
} 
