package com.uam.mercadito.auth;

import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDTOs {

  public record LoginRequest(
      @NotBlank @Email String username,
      @NotBlank @Size(min = 6, max = 64) String password
  ) {}

  public record RegisterRequest(
      @NotBlank @Email String email,
      @NotBlank @Size(min = 6, max = 64) String password
  ) {}

  public record TokenResponse(
      String accessToken
  ) {}

  public record MeResponse(
      String user,
      List<String> roles
  ) {
    public MeResponse(String user, java.util.Collection<String> roles) {
      this(user, List.copyOf(roles));
    }
  }
}
