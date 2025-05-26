package com.commerce.demo.integration.api;

import com.commerce.demo.web.dto.BrandResponse;
import com.commerce.demo.web.dto.CreateBrandRequest;
import com.commerce.demo.web.dto.ProductRequest;
import com.commerce.demo.web.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductApiIntegrationTest {
  
  @Autowired
  TestRestTemplate restTemplate;
  Long brandId;
  
  @BeforeEach
  void setUp() {
    // 브랜드 생성
    var brandRequest = new HttpEntity<>(new CreateBrandRequest("Nike"));
    ResponseEntity<BrandResponse> brandResponse = restTemplate.exchange(
            "/api/v1/brands",
            HttpMethod.POST,
            brandRequest,
            new ParameterizedTypeReference<>() {
            }
    );
    brandId = brandResponse.getBody().id();
  }
  
  @Test
  @DisplayName("상품을 생성하고, 조회/수정/삭제 API로 검증한다")
  void productCrud() {
    // 1. 상품 생성 - 실패 케이스: 잘못된 브랜드 ID
    ProductRequest invalidBrandReq = new ProductRequest("티셔츠", "상의", 10000, 999L);
    var invalidBrandEntity = new HttpEntity<>(invalidBrandReq);
    ResponseEntity<ProductResponse> invalidBrandResponse = restTemplate.exchange(
            "/api/v1/products",
            HttpMethod.POST,
            invalidBrandEntity,
            new ParameterizedTypeReference<>() {
            }
    );
    assertThat(invalidBrandResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    
    // 2. 상품 생성 - 실패 케이스: 잘못된 가격
    ProductRequest invalidPriceReq = new ProductRequest("티셔츠", "상의", -1000, brandId);
    var invalidPriceEntity = new HttpEntity<>(invalidPriceReq);
    ResponseEntity<ProductResponse> invalidPriceResponse = restTemplate.exchange(
            "/api/v1/products",
            HttpMethod.POST,
            invalidPriceEntity,
            new ParameterizedTypeReference<>() {
            }
    );
    assertThat(invalidPriceResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    
    // 3. 상품 생성 - 성공 케이스
    ProductRequest createReq = new ProductRequest("티셔츠", "상의", 10000, brandId);
    var createEntity = new HttpEntity<>(createReq);
    ResponseEntity<ProductResponse> createResponse = restTemplate.exchange(
            "/api/v1/products",
            HttpMethod.POST,
            createEntity,
            new ParameterizedTypeReference<>() {
            }
    );
    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    Long productId = createResponse.getBody().id();
    assertThat(createResponse.getBody().name()).isEqualTo("티셔츠");
    
    // 4. 상품 단건 조회
    ResponseEntity<ProductResponse> getResponse = restTemplate.exchange(
            "/api/v1/products/" + productId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            }
    );
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(getResponse.getBody().name()).isEqualTo("티셔츠");
    
    // 5. 상품 수정
    ProductRequest updateReq = new ProductRequest("셔츠", "상의", 12000, brandId);
    var updateEntity = new HttpEntity<>(updateReq);
    ResponseEntity<ProductResponse> updateResponse = restTemplate.exchange(
            "/api/v1/products/" + productId,
            HttpMethod.PUT,
            updateEntity,
            new ParameterizedTypeReference<>() {
            }
    );
    assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(updateResponse.getBody().name()).isEqualTo("셔츠");
    assertThat(updateResponse.getBody().price()).isEqualTo(12000);
    
    // 6. 상품 삭제
    ResponseEntity<Void> deleteResponse = restTemplate.exchange(
            "/api/v1/products/" + productId,
            HttpMethod.DELETE,
            null,
            new ParameterizedTypeReference<>() {
            }
    );
    assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    
    // 7. 삭제 후 조회시 404 반환
    ResponseEntity<ProductResponse> getAfterDelete = restTemplate.exchange(
            "/api/v1/products/" + productId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            }
    );
    assertThat(getAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
} 