package com.uam.mercadito.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository repo;

  public List<Category> findAll() {
    return repo.findAll();
  }

  public Category findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
  }
}
