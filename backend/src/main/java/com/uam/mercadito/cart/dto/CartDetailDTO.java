package com.uam.mercadito.cart.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record CartDetailDTO(
    Long id,
    Long userId,
    String status,
    BigDecimal totalAmount,
    Instant createdAt,
    Set<CartItemDTO> items
) {}