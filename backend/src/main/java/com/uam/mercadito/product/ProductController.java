package com.uam.mercadito.product;

import com.uam.mercadito.product.dto.ProductDetailDTO;
import com.uam.mercadito.product.dto.ProductListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService service;

  @GetMapping
  public Page<ProductListDTO> list(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) Long categoryId,
      Pageable pageable) {
    return service.list(q, categoryId, pageable);
  }

  @GetMapping("/{id}")
  public ProductDetailDTO get(@PathVariable Long id) {
    return service.get(id);
  }
}