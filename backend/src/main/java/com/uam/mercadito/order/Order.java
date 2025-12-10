package com.uam.mercadito.order;

import com.uam.mercadito.user.AppUser;
import com.uam.mercadito.user.Location;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String status; // PENDING, PAID, DELIVERED, CANCELLED

    @Column(nullable = false)
    private BigDecimal total;

    @Column(name = "payment_method")
    private String paymentMethod; // CARD, CASH, TRANSFER

    // Relación con la ubicación elegida (Snapshot o Referencia)
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}