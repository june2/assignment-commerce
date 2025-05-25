package com.commerce.demo.application;

import com.commerce.demo.domain.brand.Brand;
import com.commerce.demo.domain.brand.BrandRepository;
import com.commerce.demo.web.dto.BrandResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandService {
  
  private final BrandRepository brandRepository;
  
  public BrandResponse create(String name) {
    Brand brand = brandRepository.save(new Brand(null, name));
    return BrandResponse.from(brand);
  }
  
  public List<BrandResponse> findAll() {
    return brandRepository.findAll().stream()
            .map(BrandResponse::from)
            .toList();
  }
  
  public BrandResponse findById(Long id) {
    Brand brand = brandRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("브랜드 없음"));
    return BrandResponse.from(brand);
  }
  
  public BrandResponse update(Long id, String name) {
    Brand brand = brandRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("브랜드 없음"));
    Brand updated = new Brand(brand.getId(), name);
    Brand saved = brandRepository.save(updated);
    return BrandResponse.from(saved);
  }
  
  public void delete(Long id) {
    brandRepository.deleteById(id);
  }
}
