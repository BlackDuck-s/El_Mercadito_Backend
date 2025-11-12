package com.uam.mercadito.product;

import com.uam.mercadito.product.dto.ProductDetailDTO;
import com.uam.mercadito.product.dto.ProductListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository repo;

  public Page<ProductListDTO> list(String q, Long categoryId, Pageable pageable) {
    String query = (q == null || q.isBlank()) ? null : q.trim();
    return repo.search(query, categoryId, pageable);
  }

  public ProductDetailDTO get(Long id) {
    return repo.findDetailById(id)
        .orElseThrow(() -> new RuntimeException("Product not found"));
  }
}
