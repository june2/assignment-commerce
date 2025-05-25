package com.commerce.demo.domain.price;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ProductWithBrand {

  private Long id;
  private String category;
  private String name;
  private Long price;
  private Long brandId;
  private String brandName;
} 
