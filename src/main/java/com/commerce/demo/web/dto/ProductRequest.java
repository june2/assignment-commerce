package com.commerce.demo.web.dto;

import com.commerce.demo.domain.product.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
    @NotBlank(message = "상품명은 필수입니다")
    String name,
    
    @NotNull(message = "카테고리는 필수입니다")
    Category category,
    
    @Min(value = 0, message = "가격은 0 이상이어야 합니다")
    long price,
    
    @NotNull(message = "브랜드 ID는 필수입니다")
    Long brandId
) {} 