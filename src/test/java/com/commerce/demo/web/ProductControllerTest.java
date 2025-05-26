package com.commerce.demo.web;

import com.commerce.demo.application.ProductService;
import com.commerce.demo.domain.exception.BrandNotFoundException;
import com.commerce.demo.domain.exception.ProductNotFoundException;
import com.commerce.demo.web.dto.ProductRequest;
import com.commerce.demo.web.dto.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService productService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("상품 생성 성공")
  void createProduct_Success() throws Exception {
    // given
    ProductRequest request = new ProductRequest("테스트 상품", "상의", 10000L, 1L);
    ProductResponse response = new ProductResponse(1L, "테스트 상품", "상의", 10000L, 1L, "브랜드A");

    Mockito.when(productService.create(any())).thenReturn(response);

    // when & then
    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("테스트 상품"));
  }

  @Test
  @DisplayName("상품 생성 실패 - 브랜드 없음")
  void createProduct_BrandNotFound() throws Exception {
    // given
    ProductRequest request = new ProductRequest("테스트 상품", "상의", 10000L, 999L);
    Mockito.when(productService.create(any())).thenThrow(new BrandNotFoundException(999L));

    // when & then
    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("상품 조회 성공")
  void findProduct_Success() throws Exception {
    // given
    ProductResponse response = new ProductResponse(1L, "테스트 상품", "상의", 10000L, 1L, "브랜드A");
    Mockito.when(productService.findById(eq(1L))).thenReturn(response);

    // when & then
    mockMvc.perform(get("/api/v1/products/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  @DisplayName("상품 조회 실패 - 상품 없음")
  void findProduct_NotFound() throws Exception {
    // given
    Mockito.when(productService.findById(eq(1L))).thenThrow(new ProductNotFoundException(1L));

    // when & then
    mockMvc.perform(get("/api/v1/products/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("상품 수정 성공")
  void updateProduct_Success() throws Exception {
    // given
    ProductRequest request = new ProductRequest("수정된 상품", "상의", 15000L, 1L);
    ProductResponse response = new ProductResponse(1L, "수정된 상품", "상의", 15000L, 1L, "브랜드A");

    Mockito.when(productService.update(eq(1L), any())).thenReturn(response);

    // when & then
    mockMvc.perform(put("/api/v1/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("수정된 상품"));
  }

  @Test
  @DisplayName("상품 수정 실패 - 상품 없음")
  void updateProduct_NotFound() throws Exception {
    // given
    ProductRequest request = new ProductRequest("수정된 상품", "상의", 15000L, 1L);
    Mockito.when(productService.update(eq(1L), any()))
        .thenThrow(new ProductNotFoundException(1L));

    // when & then
    mockMvc.perform(put("/api/v1/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("상품 삭제 실패 - 상품 없음")
  void deleteProduct_NotFound() throws Exception {
    // given
    Mockito.doThrow(new ProductNotFoundException(1L)).when(productService).delete(1L);

    // when & then
    mockMvc.perform(delete("/api/v1/products/1"))
        .andExpect(status().isNotFound());
  }
}
