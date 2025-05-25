package com.commerce.demo.application;

import com.commerce.demo.domain.brand.Brand;
import com.commerce.demo.domain.brand.BrandRepository;
import com.commerce.demo.domain.product.Product;
import com.commerce.demo.domain.product.ProductRepository;
import com.commerce.demo.domain.product.Money;
import com.commerce.demo.web.dto.ProductRequest;
import com.commerce.demo.web.dto.ProductResponse;
import com.commerce.demo.web.dto.CategoryLowestPriceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class ProductServiceTest {
  
  private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
  private final BrandRepository brandRepository = Mockito.mock(BrandRepository.class);
  private final ProductService productService = new ProductService(productRepository,
          brandRepository);
  
  private Brand brandA;
  private Brand brandC;
  private Brand brandD;
  private Brand brandE;
  private Brand brandF;
  private Brand brandG;
  private Brand brandI;
  
  @BeforeEach
  void setUp() {
    brandA = new Brand(1L, "A");
    brandC = new Brand(3L, "C");
    brandD = new Brand(4L, "D");
    brandE = new Brand(5L, "E");
    brandF = new Brand(6L, "F");
    brandG = new Brand(7L, "G");
    brandI = new Brand(9L, "I");
  }
  
  @Test
  @DisplayName("상품 생성이 정상 동작한다")
  void create() {
    ProductRequest req = new ProductRequest("티셔츠", "상의", 10000, 1L);
    Mockito.when(brandRepository.findById(1L)).thenReturn(Optional.of(brandA));
    Mockito.when(productRepository.save(Mockito.any()))
            .thenReturn(new Product(1L, "상의", "티셔츠", new Money(10000), brandA));
    ProductResponse response = productService.create(req);
    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("티셔츠");
    assertThat(response.category()).isEqualTo("상의");
    assertThat(response.price()).isEqualTo(10000);
    assertThat(response.brandId()).isEqualTo(1L);
    assertThat(response.brandName()).isEqualTo("A");
  }
  
  @Test
  @DisplayName("전체 상품 조회가 정상 동작한다")
  void findAll() {
    Mockito.when(productRepository.findAll()).thenReturn(List.of(
            new Product(1L, "상의", "티셔츠", new Money(10000), brandA),
            new Product(2L, "하의", "바지", new Money(20000), brandA)
    ));
    List<ProductResponse> result = productService.findAll();
    assertThat(result).hasSize(2);
    assertThat(result).anyMatch(p -> p.name().equals("티셔츠"));
    assertThat(result).anyMatch(p -> p.name().equals("바지"));
  }
  
  @Test
  @DisplayName("ID로 상품 단건 조회가 정상 동작한다")
  void findById() {
    Mockito.when(productRepository.findById(1L))
            .thenReturn(Optional.of(new Product(1L, "상의", "티셔츠", new Money(10000), brandA)));
    ProductResponse response = productService.findById(1L);
    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("티셔츠");
  }
  
  @Test
  @DisplayName("존재하지 않는 ID로 상품 조회시 예외가 발생한다")
  void findByIdNotFound() {
    Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> productService.findById(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("상품 없음");
  }
  
  @Test
  @DisplayName("상품 수정이 정상 동작한다")
  void update() {
    Product oldProduct = new Product(1L, "상의", "티셔츠", new Money(10000), brandA);
    ProductRequest req = new ProductRequest("셔츠", "상의", 12000, 1L);
    Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(oldProduct));
    Mockito.when(brandRepository.findById(1L)).thenReturn(Optional.of(brandA));
    Mockito.when(productRepository.save(Mockito.any()))
            .thenReturn(new Product(1L, "상의", "셔츠", new Money(12000), brandA));
    ProductResponse response = productService.update(1L, req);
    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("셔츠");
    assertThat(response.price()).isEqualTo(12000);
  }
  
  @Test
  @DisplayName("존재하지 않는 상품 수정시 예외가 발생한다")
  void updateNotFound() {
    ProductRequest req = new ProductRequest("셔츠", "상의", 12000, 1L);
    Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> productService.update(1L, req))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("상품 없음");
  }
  
  @Test
  @DisplayName("존재하지 않는 브랜드로 상품 수정시 예외가 발생한다")
  void updateBrandNotFound() {
    Product oldProduct = new Product(1L, "상의", "티셔츠", new Money(10000), brandA);
    ProductRequest req = new ProductRequest("셔츠", "상의", 12000, 2L);
    Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(oldProduct));
    Mockito.when(brandRepository.findById(2L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> productService.update(1L, req))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("브랜드 없음");
  }
  
  @Test
  @DisplayName("상품 삭제가 정상 동작한다")
  void delete() {
    Mockito.doNothing().when(productRepository).deleteById(1L);
    productService.delete(1L);
    Mockito.verify(productRepository).deleteById(1L);
  }
  
  
  private void assertContainsCategory(List<CategoryLowestPriceResponse> items,
          String category,
          String expectedBrand,
          long expectedPrice) {
    assertThat(items.stream()
            .filter(item -> item.category().equals(category))
            .findFirst()
            .map(item -> {
              assertThat(item.brandName()).isEqualTo(expectedBrand);
              assertThat(item.price()).isEqualTo(expectedPrice);
              return true;
            })
            .orElse(false)).isTrue();
  }
} 
