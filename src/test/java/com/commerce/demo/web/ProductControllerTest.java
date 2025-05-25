package com.commerce.demo.web;

import com.commerce.demo.application.ProductService;
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
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;
  @MockBean
  ProductService productService;

  @Test
  @DisplayName("상품 생성 API는 정상적으로 ProductResponse를 반환한다")
  void createProduct() throws Exception {
    Mockito.when(productService.create(any()))
        .thenReturn(new ProductResponse(1L, "티셔츠", "상의", 10000, 1L, "Nike"));
    ProductRequest req = new ProductRequest("티셔츠", "상의", 10000, 1L);
    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("티셔츠"));
  }

  @Test
  @DisplayName("상품 생성 API는 예외 발생시 400을 반환한다")
  void createProductException() throws Exception {
    Mockito.when(productService.create(any())).thenThrow(new IllegalArgumentException("브랜드 없음"));
    ProductRequest req = new ProductRequest("티셔츠", "상의", 10000, 1L);
    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("상품 단건 조회 API는 정상적으로 ProductResponse를 반환한다")
  void getProduct() throws Exception {
    Mockito.when(productService.findById(eq(1L)))
        .thenReturn(new ProductResponse(1L, "티셔츠", "상의", 10000, 1L, "Nike"));
    mockMvc.perform(get("/api/v1/products/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("티셔츠"));
  }

  @Test
  @DisplayName("상품 단건 조회 API는 예외 발생시 400을 반환한다")
  void getProductException() throws Exception {
    Mockito.when(productService.findById(eq(1L))).thenThrow(new IllegalArgumentException("상품 없음"));
    mockMvc.perform(get("/api/v1/products/1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("상품 수정 API는 정상적으로 ProductResponse를 반환한다")
  void updateProduct() throws Exception {
    Mockito.when(productService.update(eq(1L), any()))
        .thenReturn(new ProductResponse(1L, "셔츠", "상의", 12000, 1L, "Nike"));
    ProductRequest req = new ProductRequest("셔츠", "상의", 12000, 1L);
    mockMvc.perform(put("/api/v1/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("셔츠"));
  }

  @Test
  @DisplayName("상품 수정 API는 예외 발생시 400을 반환한다")
  void updateProductException() throws Exception {
    Mockito.when(productService.update(eq(1L), any()))
        .thenThrow(new IllegalArgumentException("상품 없음"));
    ProductRequest req = new ProductRequest("셔츠", "상의", 12000, 1L);
    mockMvc.perform(put("/api/v1/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("상품 삭제 API는 정상적으로 200을 반환한다")
  void deleteProduct() throws Exception {
    Mockito.doNothing().when(productService).delete(1L);
    mockMvc.perform(delete("/api/v1/products/1"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("상품 삭제 API는 예외 발생시 400을 반환한다")
  void deleteProductException() throws Exception {
    Mockito.doThrow(new IllegalArgumentException("상품 없음")).when(productService).delete(1L);
    mockMvc.perform(delete("/api/v1/products/1"))
        .andExpect(status().isBadRequest());
  }
}
