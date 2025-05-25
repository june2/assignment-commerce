package com.commerce.demo.domain.brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {

  Optional<Brand> findById(Long id);

  List<Brand> findAll();

  Brand save(Brand brand);

  void deleteById(Long id);
}
