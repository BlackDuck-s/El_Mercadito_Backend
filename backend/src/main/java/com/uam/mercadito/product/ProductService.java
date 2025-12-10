package com.uam.mercadito.product;

import com.uam.mercadito.category.Category;
import com.uam.mercadito.category.CategoryRepository;
import com.uam.mercadito.product.dto.ProductDetailDTO;
import com.uam.mercadito.product.dto.ProductListDTO;
import com.uam.mercadito.product.dto.ProductCreateDTO;
import com.uam.mercadito.product.dto.ProductUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.uam.mercadito.user.AppUserRepository;
import com.uam.mercadito.user.AppUser;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository repo;
  private final CategoryRepository categories;
  private final AppUserRepository users;

  public Page<ProductListDTO> listMyProducts(String email, Pageable pageable) {
      return repo.findBySellerEmail(email, pageable);
  }

  public Page<ProductListDTO> list(String q, Long categoryId, Pageable pageable) {
    String pattern = null;
    if (q != null && !q.isBlank()) {
        pattern = "%" + q.trim() + "%";
    }
    return repo.search(pattern, categoryId, pageable);
  }

  public ProductDetailDTO get(Long id) {
    return repo.findDetailById(id)
        .orElseThrow(() -> new RuntimeException("Product not found"));
  }

  public Long create(ProductCreateDTO dto, String sellerEmail) {
    Category cat = categories.findById(dto.categoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));

    AppUser seller = users.findByEmail(sellerEmail)
        .orElseThrow(() -> new RuntimeException("Seller not found"));

    var p = Product.builder()
        .name(dto.name())
        .description(dto.description())
        .price(dto.price())
        .stock(dto.stock())
        .category(cat)
        .seller(seller)
        .createdAt(LocalDateTime.now())
        .build();

    return repo.save(p).getId();
  }

  public void update(Long id, ProductUpdateDTO dto, String currentUserEmail, boolean isAdmin) {
    var p = repo.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found"));

    if (!isAdmin && !p.getSeller().getEmail().equals(currentUserEmail)) {
        throw new RuntimeException("You are not authorized to update this product");
    }

    if (dto.name() != null) p.setName(dto.name());
    if (dto.description() != null) p.setDescription(dto.description());
    if (dto.price() != null) p.setPrice(dto.price());
    if (dto.stock() != null) p.setStock(dto.stock());
    if (dto.categoryId() != null) {
      var cat = categories.findById(dto.categoryId())
          .orElseThrow(() -> new RuntimeException("Category not found"));
      p.setCategory(cat);
    }
    p.setUpdatedAt(LocalDateTime.now());
    repo.save(p);
  }

  public void delete(Long id, String currentUserEmail, boolean isAdmin) {
    var p = repo.findById(id)
         .orElseThrow(() -> new RuntimeException("Product not found"));
    if (!isAdmin && !p.getSeller().getEmail().equals(currentUserEmail)) {
        throw new RuntimeException("You are not authorized to delete this product");
    }

    repo.delete(p);
  }
}