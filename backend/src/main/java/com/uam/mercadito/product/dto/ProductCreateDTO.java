package com.uam.mercadito.product.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductCreateDTO(
    @NotBlank @Size(max = 160) String name,
    @Size(max = 1000) String description,
    @NotNull @DecimalMin(value = "0.01") BigDecimal price,
    @NotNull @Min(0) Integer stock,
    @NotNull @Positive Long categoryId) {
}
