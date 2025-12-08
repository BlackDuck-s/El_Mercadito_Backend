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

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository repo;
  private final CategoryRepository categories;

  /* -------- lectura -------- */
  public Page<ProductListDTO> list(String q, Long categoryId, Pageable pageable) {
    String query = (q == null || q.isBlank()) ? null : q.trim();
    return repo.search(query, categoryId, pageable);
  }

  public ProductDetailDTO get(Long id) {
    return repo.findDetailById(id)
        .orElseThrow(() -> new RuntimeException("Product not found"));
  }

  /* -------- escritura -------- */
  public Long create(ProductCreateDTO dto) {
    Category cat = categories.findById(dto.categoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));

    var p = Product.builder()
        .name(dto.name())
        .description(dto.description())
        .price(dto.price())
        .stock(dto.stock())
        .category(cat)
        .createdAt(Instant.now())
        .build();

    return repo.save(p).getId();
  }

  public void update(Long id, ProductUpdateDTO dto) {
    var p = repo.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found"));

    if (dto.name() != null)
      p.setName(dto.name());
    if (dto.description() != null)
      p.setDescription(dto.description());
    if (dto.price() != null)
      p.setPrice(dto.price());
    if (dto.stock() != null)
      p.setStock(dto.stock());
    if (dto.categoryId() != null) {
      var cat = categories.findById(dto.categoryId())
          .orElseThrow(() -> new RuntimeException("Category not found"));
      p.setCategory(cat);
    }
    p.setUpdatedAt(Instant.now());
    repo.save(p);
  }

  public void delete(Long id) {
    if (!repo.existsById(id))
      throw new RuntimeException("Product not found");
    repo.deleteById(id);
  }
}
