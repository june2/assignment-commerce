package com.commerce.demo.infrastructure.product;

import com.commerce.demo.domain.product.Product;
import com.commerce.demo.domain.product.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

  private final ProductJpaRepository productJpaRepository;

  @Override
  public Optional<Product> findById(Long id) {
    return productJpaRepository.findById(id)
        .map(ProductJpaEntity::toDomain);
  }

  @Override
  public List<Product> findAll() {
    return productJpaRepository.findAll().stream()
        .map(ProductJpaEntity::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public Product save(Product product) {
    ProductJpaEntity entity = new ProductJpaEntity(product.getId(), product.getCategory(),
        product.getName(),
        product.getPrice().getValue(),
        product.getBrand() != null ? product.getBrand().getId() : null);
    ProductJpaEntity saved = productJpaRepository.save(entity);
    return saved.toDomain();
  }

  @Override
  public void deleteById(Long id) {
    productJpaRepository.deleteById(id);
  }
} 
