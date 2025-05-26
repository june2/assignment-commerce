package com.commerce.demo.application;

import com.commerce.demo.domain.brand.Brand;
import com.commerce.demo.domain.brand.BrandRepository;
import com.commerce.demo.domain.exception.BrandNotFoundException;
import com.commerce.demo.web.dto.BrandResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class BrandServiceTest {
  
  private final BrandRepository brandRepository = Mockito.mock(BrandRepository.class);
  private final BrandService brandService = new BrandService(brandRepository);
  
  @Test
  @DisplayName("브랜드 생성이 정상 동작한다")
  void create() {
    Mockito.when(brandRepository.save(Mockito.any())).thenReturn(new Brand(1L, "Nike"));
    BrandResponse response = brandService.create("Nike");
    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("Nike");
  }
  
  @Test
  @DisplayName("전체 브랜드 조회가 정상 동작한다")
  void findAll() {
    Mockito.when(brandRepository.findAll())
            .thenReturn(List.of(new Brand(1L, "Nike"), new Brand(2L, "Adidas")));
    List<BrandResponse> result = brandService.findAll();
    assertThat(result).hasSize(2);
    assertThat(result).anyMatch(b -> b.name().equals("Nike"));
    assertThat(result).anyMatch(b -> b.name().equals("Adidas"));
  }
  
  @Test
  @DisplayName("ID로 브랜드 단건 조회가 정상 동작한다")
  void findById() {
    Mockito.when(brandRepository.findById(1L)).thenReturn(Optional.of(new Brand(1L, "Nike")));
    BrandResponse response = brandService.findById(1L);
    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("Nike");
  }
  
  @Test
  @DisplayName("존재하지 않는 ID로 브랜드 조회시 예외가 발생한다")
  void findByIdNotFound() {
    Mockito.when(brandRepository.findById(1L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> brandService.findById(1L))
            .isInstanceOf(BrandNotFoundException.class)
            .hasMessage("브랜드(id=1)를 찾을 수 없습니다");
  }
  
  @Test
  @DisplayName("브랜드 이름 수정이 정상 동작한다")
  void update() {
    Mockito.when(brandRepository.findById(1L)).thenReturn(Optional.of(new Brand(1L, "Nike")));
    Mockito.when(brandRepository.save(Mockito.any())).thenReturn(new Brand(1L, "Adidas"));
    BrandResponse response = brandService.update(1L, "Adidas");
    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("Adidas");
  }
  
  @Test
  @DisplayName("존재하지 않는 ID로 브랜드 수정시 예외가 발생한다")
  void updateNotFound() {
    Mockito.when(brandRepository.findById(1L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> brandService.update(1L, "Adidas"))
            .isInstanceOf(BrandNotFoundException.class)
            .hasMessage("브랜드(id=1)를 찾을 수 없습니다");
  }
  
  @Test
  @DisplayName("브랜드 삭제가 정상 동작한다")
  void delete() {
    Brand brand = new Brand(1L, "Nike");
    Mockito.when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    Mockito.doNothing().when(brandRepository).deleteById(1L);
    brandService.delete(1L);
    Mockito.verify(brandRepository).deleteById(1L);
  }
} 