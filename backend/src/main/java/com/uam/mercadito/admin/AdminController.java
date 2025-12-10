package com.uam.mercadito.admin;

import com.uam.mercadito.cart.ShoppingCartRepository; // Asegúrate de importar esto
import com.uam.mercadito.product.ProductRepository; // Asegúrate de importar esto
import com.uam.mercadito.role.RoleRepository;
import com.uam.mercadito.user.AppUser;
import com.uam.mercadito.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final SellerRequestRepository requestRepository;
  private final AppUserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ProductRepository productRepository;
  private final ShoppingCartRepository cartRepository;

  record SellerRequestDTO(Long id, String userName, String email, String requestDate, String status,
      boolean documentsAttached) {
  }

  record DashboardStatsDTO(long productsActive, BigDecimal salesTotal, long newUsersToday) {
  }

  @GetMapping("/stats")
    public DashboardStatsDTO getDashboardStats() {
        long totalProducts = productRepository.count();

        BigDecimal totalSales = cartRepository.findAll().stream()
                .filter(cart -> cart != null && cart.getStatus() != null && !"PENDING".equals(cart.getStatus()))
                .map(cart -> cart.getTotalAmount() != null ? cart.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Instant twentyFourHoursAgo = Instant.now().minus(1, ChronoUnit.DAYS);
        
        long newUsers = userRepository.findAll().stream()
                .filter(user -> user.getCreatedAt() != null && user.getCreatedAt().isAfter(twentyFourHoursAgo))
                .count();

        return new DashboardStatsDTO(totalProducts, totalSales, newUsers);
    }

    @GetMapping("/requests")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<SellerRequestDTO> getAllRequests() {
        return requestRepository.findAll().stream()
            .map(req -> new SellerRequestDTO(
                req.getId(),
                req.getUser().getName(),
                req.getUser().getEmail(),
                req.getRequestDate().toString(),
                req.getStatus(),
                Boolean.TRUE.equals(req.getDocumentsAttached()) 
            ))
            .collect(Collectors.toList());
    }

  @PostMapping("/requests/{id}/approve")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> approveRequest(@PathVariable Long id) {
    SellerRequest request = requestRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Request not found"));

    if (!"PENDING".equals(request.getStatus())) {
      throw new RuntimeException("Request is not pending");
    }

    request.setStatus("APPROVED");
    requestRepository.save(request);

    AppUser user = request.getUser();
    var sellerRole = roleRepository.findByName("SELLER")
        .orElseThrow(() -> new RuntimeException("Role SELLER not found"));

    user.getRoles().add(sellerRole);
    userRepository.save(user);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/requests/{id}/reject")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> rejectRequest(@PathVariable Long id) {
    SellerRequest request = requestRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Request not found"));

    request.setStatus("REJECTED");
    requestRepository.save(request);

    return ResponseEntity.ok().build();
  }
}