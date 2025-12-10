package com.uam.mercadito.admin;

import com.uam.mercadito.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SellerRequestRepository extends JpaRepository<SellerRequest, Long> {
    List<SellerRequest> findByStatus(String status);
    Optional<SellerRequest> findByUser(AppUser user);
}