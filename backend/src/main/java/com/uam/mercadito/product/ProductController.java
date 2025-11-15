package com.uam.mercadito.product;

import com.uam.mercadito.product.dto.ProductDetailDTO;
import com.uam.mercadito.product.dto.ProductListDTO;
import com.uam.mercadito.product.dto.ProductCreateDTO;
import com.uam.mercadito.product.dto.ProductUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

  private final ProductService service;

  /* -------- lectura pública (ya expuesta en SecurityConfig) -------- */
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

  /* -------- escritura (SELLER/ADMIN) -------- */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
  public Long create(@Valid @RequestBody ProductCreateDTO dto) {
    return service.create(dto);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
  public void update(@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO dto) {
    service.update(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
