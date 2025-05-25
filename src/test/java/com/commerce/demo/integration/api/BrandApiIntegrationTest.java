package com.commerce.demo.integration.api;

import com.commerce.demo.web.dto.BrandResponse;
import com.commerce.demo.web.dto.CreateBrandRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BrandApiIntegrationTest {
  
  @Autowired
  TestRestTemplate restTemplate;
  
  @Test
  @DisplayName("브랜드를 생성하고, 조회 API로 검증한다")
  void createAndGetBrand() {
    // 1. 브랜드 생성
    var request = new HttpEntity<>(new CreateBrandRequest("Nike"));
    ResponseEntity<BrandResponse> createResponse = restTemplate.exchange(
            "/api/v1/brands",
            HttpMethod.POST,
            request,
            new ParameterizedTypeReference<>() {
            }
    );
    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    Long id = createResponse.getBody().id();
    
    // 2. 브랜드 단건 조회
    ResponseEntity<BrandResponse> getResponse = restTemplate.exchange(
            "/api/v1/brands/" + id,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            }
    );
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(getResponse.getBody().name()).isEqualTo("Nike");
  }
} 