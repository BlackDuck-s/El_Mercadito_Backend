package com.uam.mercadito.auth;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uam.mercadito.admin.SellerRequest;
import com.uam.mercadito.admin.SellerRequestRepository;
import com.uam.mercadito.auth.AuthDTOs.LoginRequest;
import com.uam.mercadito.auth.AuthDTOs.MeResponse;
import com.uam.mercadito.auth.AuthDTOs.RegisterRequest;
import com.uam.mercadito.auth.AuthDTOs.TokenResponse;
import com.uam.mercadito.role.Role;
import com.uam.mercadito.role.RoleRepository;
import com.uam.mercadito.security.JwtUtils;
import com.uam.mercadito.user.AppUser;
import com.uam.mercadito.user.AppUserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authManager;
  private final JwtUtils jwtUtils;
  private final AppUserRepository users;
  private final RoleRepository roles;
  private final PasswordEncoder passwordEncoder;

  /* ---------- LOGIN ---------- */
  @PostMapping("/login")
  public TokenResponse login(@Valid @RequestBody LoginRequest request) {
    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    User principal = (User) auth.getPrincipal();
    String token = jwtUtils.generateToken(
        principal.getUsername(),
        principal.getAuthorities());

    return new TokenResponse(token);
  }

  /* ---------- REGISTER ---------- */
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public TokenResponse register(@Valid @RequestBody RegisterRequest request) {

    String email = request.email().toLowerCase().trim();
    String name = request.name().trim();
    String phone = request.phone();
    String password = request.password();
    String confirmPassword = request.confirmPassword();

    if (users.findByEmail(email).isPresent()) {
      throw new RuntimeException("Email already in use");
    }

    if (!password.equals(confirmPassword)) {
      throw new RuntimeException("Passwords do not match");
    }

    if (name.isEmpty() || phone.isEmpty()) {
      throw new RuntimeException("Full name and phone number are required");
    }

    Role clientRole = roles.findByName("CLIENT")
        .orElseThrow(() -> new RuntimeException("Role CLIENT not found"));

    AppUser user = AppUser.builder()
        .email(email)
        .name(name)
        .phone(phone)
        .password(passwordEncoder.encode(request.password()))
        .enabled(true)
        .roles(Set.of(clientRole))
        .build();

    user = users.save(user);

    var authorities = user.getRoles().stream()
        .map(r -> "ROLE_" + r.getName())
        .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    String token = jwtUtils.generateToken(user.getEmail(), authorities);

    return new TokenResponse(token);
  }

  @GetMapping("/me")
  public MeResponse me(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
    if (principal == null) {
      return new MeResponse(null, java.util.List.of());
    }
    var roles = principal.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return new MeResponse(principal.getUsername(), roles);
  }

  private final SellerRequestRepository requestRepository;

  @PostMapping("/request-seller-role")
  public ResponseEntity<Void> requestSellerRole(@AuthenticationPrincipal User principal) {
    AppUser user = users.findByEmail(principal.getUsername())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (user.getRoles().stream().anyMatch(r -> r.getName().equals("SELLER"))) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    Optional<SellerRequest> existingRequest = requestRepository.findByUser(user);
    if (existingRequest.isPresent() && existingRequest.get().getStatus().equals("PENDING")) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    SellerRequest request = SellerRequest.builder()
        .user(user)
        .status("PENDING")
        .build();

    requestRepository.save(request);

    return ResponseEntity.ok().build();
  }
}