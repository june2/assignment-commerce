package com.commerce.demo.application;

import com.commerce.demo.domain.brand.Brand;
import com.commerce.demo.domain.brand.BrandRepository;
import com.commerce.demo.domain.exception.BrandNotFoundException;
import com.commerce.demo.domain.exception.ProductNotFoundException;
import com.commerce.demo.domain.product.Money;
import com.commerce.demo.domain.product.Product;
import com.commerce.demo.domain.product.ProductRepository;
import com.commerce.demo.web.dto.ProductRequest;
import com.commerce.demo.web.dto.ProductResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

  private final ProductRepository productRepository;
  private final BrandRepository brandRepository;

  public ProductResponse create(ProductRequest request) {
    Brand brand = brandRepository.findById(request.brandId())
        .orElseThrow(() -> new BrandNotFoundException(request.brandId()));
    Product product = new Product(null, request.category(), request.name(),
        new Money(request.price()), brand);
    Product saved = productRepository.save(product);
    return ProductResponse.from(saved);
  }

  public List<ProductResponse> findAll() {
    return productRepository.findAll().stream().map(ProductResponse::from).toList();
  }

  public ProductResponse findById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException(id));
    return ProductResponse.from(product);
  }

  public ProductResponse update(Long id, ProductRequest request) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException(id));
    Brand brand = brandRepository.findById(request.brandId())
        .orElseThrow(() -> new BrandNotFoundException(request.brandId()));
    Product updated = new Product(product.getId(), request.category(), request.name(),
        new Money(request.price()), brand);
    Product saved = productRepository.save(updated);
    return ProductResponse.from(saved);
  }

  public void delete(Long id) {
    if (productRepository.findById(id).isEmpty()) {
      throw new ProductNotFoundException(id);
    }
    productRepository.deleteById(id);
  }
}
