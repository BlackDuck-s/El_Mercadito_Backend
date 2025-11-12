package com.uam.mercadito.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService service;

  @GetMapping
  public List<Category> list() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Category get(@PathVariable Long id) {
    return service.findById(id);
  }
}
