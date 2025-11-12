package com.uam.mercadito.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils; // requiere que tengas JwtUtils como @Component
  private final CustomUserDetailsService userService; // tu UserDetailsService

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {

    String header = req.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      try {
        var jws = jwtUtils.parse(token);
        String username = jws.getBody().getSubject();

        // Evita recrear auth si ya está autenticado
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = userService.loadUserByUsername(username);

          var auth = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (JwtException ignored) {
        // token inválido o expirado: continuamos sin autenticar
      }
    }

    chain.doFilter(req, res);
  }
}
