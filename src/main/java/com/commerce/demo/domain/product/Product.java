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
} 
