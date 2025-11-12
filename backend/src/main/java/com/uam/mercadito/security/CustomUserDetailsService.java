package com.uam.mercadito.security;

import com.uam.mercadito.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AppUserRepository users;
  private final PasswordEncoder encoder;
  private final Environment env; // para fallback en test

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // 1) Busca en BD (dev/prod)
    var opt = users.findByEmail(username);
    if (opt.isPresent()) {
      var u = opt.get();
      var authorities = u.getRoles().stream()
          .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
          .toList();

      return User.withUsername(u.getEmail())
          .password(u.getPassword())
          .authorities(authorities)
          .accountLocked(!u.isEnabled())
          .build();
    }

    // 2) Fallback en perfil test (opcional, mantiene compatibilidad con H2)
    var active = Arrays.asList(env.getActiveProfiles());
    if (active.contains("test") && "demo@uam.mx".equalsIgnoreCase(username)) {
      return User.withUsername("demo@uam.mx")
          .password(encoder.encode("123456"))
          .roles("CLIENT")
          .build();
    }

    throw new UsernameNotFoundException("User not found: " + username);
  }
}
