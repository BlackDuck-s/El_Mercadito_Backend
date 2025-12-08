package com.uam.mercadito.product;

import com.uam.mercadito.product.dto.ProductDetailDTO;
import com.uam.mercadito.product.dto.ProductListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("""
      SELECT new com.uam.mercadito.product.dto.ProductListDTO(
        p.id, p.name, p.description, p.price, p.stock, c.id, c.name
      )
      FROM Product p
      LEFT JOIN p.category c
      WHERE (:q IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%',:q,'%'))
             OR LOWER(COALESCE(p.description,'')) LIKE LOWER(CONCAT('%',:q,'%')))
        AND (:categoryId IS NULL OR c.id = :categoryId)
      """)
  Page<ProductListDTO> search(
      @Param("q") String q,
      @Param("categoryId") Long categoryId,
      Pageable pageable);

  @Query("""
      SELECT new com.uam.mercadito.product.dto.ProductDetailDTO(
        p.id, p.name, p.description, p.price, p.stock, c.id, c.name
      )
      FROM Product p
      LEFT JOIN p.category c
      WHERE p.id = :id
      """)
  Optional<ProductDetailDTO> findDetailById(@Param("id") Long id);
}
