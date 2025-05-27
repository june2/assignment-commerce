package com.commerce.demo.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBrandRequest(
    @NotBlank(message = "브랜드명은 필수입니다")
    @Size(min = 1, max = 50, message = "브랜드명은 1자 이상 50자 이하여야 합니다")
    String name
) {} 