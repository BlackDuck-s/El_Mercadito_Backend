package com.uam.mercadito.product.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductUpdateDTO(
    @Nullable @Size(max = 160) String name,
    @Nullable @Size(max = 1000) String description,
    @Nullable @DecimalMin("0.00") BigDecimal price, // permite 0.00
    @Nullable @Min(0) Integer stock,
    @Nullable @Positive Long categoryId) {
}
