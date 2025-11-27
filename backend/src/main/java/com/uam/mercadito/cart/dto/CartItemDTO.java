package com.uam.mercadito.cart.dto;

import java.math.BigDecimal;

public record CartItemDTO(
    Long id,
    Long productId, // ID del producto
    String productName, // Nombre del producto para mostrar
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subTotal // unitPrice * quantity
) {
    
}