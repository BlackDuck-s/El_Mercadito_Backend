package com.uam.mercadito.admin;

import com.uam.mercadito.user.AppUser;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seller_requests")
@Getter @Setter 
@NoArgsConstructor @AllArgsConstructor @Builder
public class SellerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    @Column(nullable = false, length = 20)
    private String status; // PENDING, APPROVED, REJECTED

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "documents_attached")
    private Boolean documentsAttached;

    @PrePersist
    protected void onCreate() {
        if (this.requestDate == null) {
            this.requestDate = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "PENDING";
        }
    }
}