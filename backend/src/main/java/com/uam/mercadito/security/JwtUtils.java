package com.uam.mercadito.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtils {

  @Value("${security.jwt.secret}")
  private String secret;

  @Value("${security.jwt.issuer}")
  private String issuer;

  @Value("${security.jwt.expiration-minutes}")
  private long expirationMinutes;

  private Key key() {
    if (secret == null || secret.length() < 32) {
      throw new IllegalStateException("security.jwt.secret must be at least 32 characters for HS256.");
    }
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String username, Collection<? extends GrantedAuthority> roles) {
    Instant now = Instant.now();
    return Jwts.builder()
        .setSubject(username)
        .setIssuer(issuer)
        .claim("roles", roles.stream().map(GrantedAuthority::getAuthority).toList())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }

  public Jws<Claims> parse(String token) {
    return Jwts.parserBuilder()
        .requireIssuer(issuer)
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token);
  }
}