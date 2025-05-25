package com.commerce.demo.infrastructure.brand;

import com.commerce.demo.domain.brand.Brand;
import com.commerce.demo.domain.brand.BrandRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepository {

  private final BrandJpaRepository brandJpaRepository;

  @Override
  public Optional<Brand> findById(Long id) {
    return brandJpaRepository.findById(id).map(BrandJpaEntity::toDomain);
  }

  @Override
  public List<Brand> findAll() {
    return brandJpaRepository.findAll().stream()
        .map(BrandJpaEntity::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public Brand save(Brand brand) {
    BrandJpaEntity entity = new BrandJpaEntity(brand.getId(), brand.getName());
    BrandJpaEntity saved = brandJpaRepository.save(entity);
    return saved.toDomain();
  }

  @Override
  public void deleteById(Long id) {
    brandJpaRepository.deleteById(id);
  }
} 
