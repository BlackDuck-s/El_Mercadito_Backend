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
import java.security.Principal;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

  @GetMapping("/my-products")
  @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
  public Page<ProductListDTO> listMyProducts(Principal principal, Pageable pageable) {
      return service.listMyProducts(principal.getName(), pageable);
  }

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
  public Long create(@Valid @RequestBody ProductCreateDTO dto, Principal principal) {
    return service.create(dto, principal.getName());
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
  public void update(@PathVariable Long id, 
                     @Valid @RequestBody ProductUpdateDTO dto, 
                     Authentication auth) {
    
    boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
    service.update(id, dto, auth.getName(), isAdmin);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
  public void delete(@PathVariable Long id, Authentication auth) {
      
    boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

    service.delete(id, auth.getName(), isAdmin);
  }
}
