package com.commerce.demo.web;

import com.commerce.demo.application.BrandService;
import com.commerce.demo.web.dto.BrandResponse;
import com.commerce.demo.web.dto.CreateBrandRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {
  
  private final BrandService brandService;
  
  @PostMapping
  public ResponseEntity<BrandResponse> create(@Valid @RequestBody CreateBrandRequest request) {
    BrandResponse response = brandService.create(request.name());
    return ResponseEntity.ok(response);
  }
  
  @GetMapping
  public ResponseEntity<List<BrandResponse>> findAll() {
    return ResponseEntity.ok(brandService.findAll());
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<BrandResponse> findById(@PathVariable Long id) {
    return ResponseEntity.ok(brandService.findById(id));
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<BrandResponse> update(@PathVariable Long id,
          @Valid @RequestBody CreateBrandRequest request) {
    return ResponseEntity.ok(brandService.update(id, request.name()));
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    brandService.delete(id);
    return ResponseEntity.ok().build();
  }
}
