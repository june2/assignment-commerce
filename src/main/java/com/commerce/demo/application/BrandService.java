package com.commerce.demo.application;

import com.commerce.demo.domain.brand.Brand;
import com.commerce.demo.domain.brand.BrandRepository;
import com.commerce.demo.domain.exception.BrandNotFoundException;
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
    Brand brand = Brand.create(name);
    Brand saved = brandRepository.save(brand);
    return BrandResponse.from(saved);
  }
  
  public List<BrandResponse> findAll() {
    return brandRepository.findAll().stream()
            .map(BrandResponse::from)
            .toList();
  }
  
  public BrandResponse findById(Long id) {
    Brand brand = brandRepository.findById(id)
            .orElseThrow(() -> new BrandNotFoundException(id));
    return BrandResponse.from(brand);
  }
  
  public BrandResponse update(Long id, String name) {
    Brand existingBrand = brandRepository.findById(id)
            .orElseThrow(() -> new BrandNotFoundException(id));
    
    Brand updatedBrand = Brand.of(existingBrand.getId(), name);
    Brand saved = brandRepository.save(updatedBrand);
    return BrandResponse.from(saved);
  }
  
  public void delete(Long id) {
    if (brandRepository.findById(id).isEmpty()) {
      throw new BrandNotFoundException(id);
    }
    brandRepository.deleteById(id);
  }
}
