package com.uam.mercadito.product.dto;

import java.math.BigDecimal;

public record ProductListDTO(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    Long categoryId,
    String categoryName) {
}
