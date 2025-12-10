package com.uam.mercadito.order;

import com.uam.mercadito.user.AppUser;
import com.uam.mercadito.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderService orderService;
    private final AppUserRepository userRepository;

    record OrderRequest(Long locationId, String paymentMethodId) {}

    @PostMapping
    public ResponseEntity<Map<String, Object>> placeOrder(
            Principal principal, 
            @RequestBody OrderRequest request
    ) {
        // Buscar usuario real
        AppUser user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Long orderId = orderService.createOrder(
                user, 
                request.locationId(), 
                request.paymentMethodId()
        );

        return ResponseEntity.ok(Map.of(
            "message", "Order confirmed successfully",
            "orderId", orderId
        ));
    }
}