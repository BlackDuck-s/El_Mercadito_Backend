package com.uam.mercadito.auth;

import com.uam.mercadito.auth.dto.LoginRequest;
import com.uam.mercadito.auth.dto.TokenResponse;
import com.uam.mercadito.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authManager;
  private final JwtUtils jwt;

  @PostMapping("/login")
  public TokenResponse login(@Valid @RequestBody LoginRequest req) {
    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
    String token = jwt.generateToken(req.getUsername(), auth.getAuthorities());
    return new TokenResponse(token);
  }

  @GetMapping("/me")
  public Map<String, Object> me(Authentication auth) {
    return Map.of(
        "user", auth != null ? auth.getName() : "anonymous",
        "roles", auth != null ? auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList() : null);
  }
}
