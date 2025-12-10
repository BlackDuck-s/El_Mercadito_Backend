package com.uam.mercadito.user;

import com.uam.mercadito.role.Role;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AppUser {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 120)
  private String email;

  @Column(nullable = false, length = 255)
  private String password;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(nullable = false, length = 255)
  private String phone;


  @Column(nullable = false)
  @Builder.Default
  private boolean enabled = true;

  @Column(name = "created_at", updatable = false)
  private Instant createdAt;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  @Builder.Default
  private Set<Role> roles = new HashSet<>();

  @PrePersist
  void onCreate() {
    this.createdAt = Instant.now();
  }
}