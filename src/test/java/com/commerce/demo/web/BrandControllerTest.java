package com.commerce.demo.web;

import com.commerce.demo.application.BrandService;
import com.commerce.demo.web.dto.BrandResponse;
import com.commerce.demo.web.dto.CreateBrandRequest;
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

@WebMvcTest(BrandController.class)
class BrandControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;
  @MockBean
  BrandService brandService;

  @Test
  @DisplayName("브랜드 생성 API는 정상적으로 BrandResponse를 반환한다")
  void createBrand() throws Exception {
    Mockito.when(brandService.create(any())).thenReturn(new BrandResponse(1L, "Nike"));
    CreateBrandRequest req = new CreateBrandRequest("Nike");
    mockMvc.perform(post("/api/v1/brands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Nike"));
  }

  @Test
  @DisplayName("브랜드 단건 조회 API는 정상적으로 BrandResponse를 반환한다")
  void getBrand() throws Exception {
    Mockito.when(brandService.findById(eq(1L))).thenReturn(new BrandResponse(1L, "Nike"));
    mockMvc.perform(get("/api/v1/brands/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Nike"));
  }
}
