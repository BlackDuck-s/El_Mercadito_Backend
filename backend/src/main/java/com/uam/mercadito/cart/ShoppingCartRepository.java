package com.uam.mercadito.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByUserIdAndStatus(Long userId, String status);

    Optional<ShoppingCart> findByUserId(Long userId);
}
