package com.commerce.demo.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

  Optional<Product> findById(Long id);

  List<Product> findAll();

  Product save(Product product);

  void deleteById(Long id);

}
