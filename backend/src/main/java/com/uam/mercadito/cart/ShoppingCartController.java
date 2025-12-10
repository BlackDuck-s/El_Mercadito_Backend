package com.uam.mercadito.cart;

import com.uam.mercadito.cart.dto.CartDetailDTO;
import com.uam.mercadito.cart.dto.CartItemAddDTO;
import com.uam.mercadito.user.AppUser;
import com.uam.mercadito.user.AppUserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // Importante: Usar Principal

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()") 
public class ShoppingCartController {

    private final ShoppingCartService service;
    private final AppUserRepository userRepository;

    // Helper para obtener el usuario real desde el Token
    private AppUser getAuthenticatedUser(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("No hay usuario autenticado");
        }
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + principal.getName()));
    }

    @GetMapping
    public CartDetailDTO getCart(Principal principal) {
        AppUser user = getAuthenticatedUser(principal);
        return service.getOrCreateCart(user.getId());
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public void addItemToCart(Principal principal, 
                              @Valid @RequestBody CartItemAddDTO dto) {
        AppUser user = getAuthenticatedUser(principal);
        service.addItem(user.getId(), dto);
    }

    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItemFromCart(Principal principal, 
                                   @PathVariable Long itemId) {
        AppUser user = getAuthenticatedUser(principal);
        service.removeItem(user.getId(), itemId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Principal principal) {
        AppUser user = getAuthenticatedUser(principal);
        service.clearCart(user.getId());
    }
    
    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.OK)
    public void checkout(Principal principal) {
        AppUser user = getAuthenticatedUser(principal);
        service.checkout(user.getId());
    }
}