package com.uam.mercadito.product.dto;

import java.math.BigDecimal;

public record ProductDetailDTO(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    Long categoryId,
    String categoryName) {
}
