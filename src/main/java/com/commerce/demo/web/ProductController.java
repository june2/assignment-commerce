package com.commerce.demo.web;

import com.commerce.demo.application.ProductService;
import com.commerce.demo.web.dto.ProductRequest;
import com.commerce.demo.web.dto.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
    ProductResponse response = productService.create(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<ProductResponse>> findAll() {
    return ResponseEntity.ok(productService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.findById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponse> update(@PathVariable Long id,
      @Valid @RequestBody ProductRequest request) {
    return ResponseEntity.ok(productService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.ok().build();
  }
}
