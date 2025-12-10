package com.uam.mercadito.order;

import com.uam.mercadito.cart.*;
import com.uam.mercadito.product.ProductRepository;
import com.uam.mercadito.user.AppUser;
import com.uam.mercadito.user.Location;
import com.uam.mercadito.user.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long createOrder(AppUser user, Long locationId, String paymentMethod) {
        // 1. Obtener Carrito Activo
        ShoppingCart cart = cartRepository.findByUserIdAndStatus(user.getId(), "PENDING")
                .orElseThrow(() -> new RuntimeException("No active cart found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. Obtener Ubicación
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        // 3. Crear la Orden
        Order order = Order.builder()
                .user(user)
                .date(LocalDateTime.now())
                .status("PENDING") // O "PAID" si el pago fuera real e inmediato
                .paymentMethod(paymentMethod)
                .location(location)
                .total(cart.getTotalAmount())
                .build();

        // 4. Convertir CartItems a OrderItems y Validar Stock
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            // Validar Stock (Doble check de seguridad)
            if (cartItem.getProduct().getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for: " + cartItem.getProduct().getName());
            }
            
            // Descontar Stock (Si no se hizo en el paso anterior)
            cartItem.getProduct().setStock(cartItem.getProduct().getStock() - cartItem.getQuantity());
            productRepository.save(cartItem.getProduct());

            return OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getUnitPrice())
                    .build();
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // 5. Vaciar y Cerrar Carrito
        cart.setStatus("COMPLETED");
        cartRepository.save(cart);
        // Opcional: Borrar items físicos del carrito si prefieres limpiar la tabla
        // cartItemRepository.deleteAllByCartId(cart.getId());

        return savedOrder.getId();
    }
}