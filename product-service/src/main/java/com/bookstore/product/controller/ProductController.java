package com.bookstore.product.controller;

import com.bookstore.product.dto.*;
import com.bookstore.product.entity.Category;
import com.bookstore.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/products")
    public Page<ProductResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getAll(page, size);
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/api/products/search")
    public List<ProductResponse> search(@RequestParam String q) {
        return productService.search(q);
    }

    @GetMapping("/api/products/category/{id}")
    public List<ProductResponse> getByCategory(@PathVariable Long id) {
        return productService.getByCategory(id);
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest req) {
        return ResponseEntity.ok(productService.create(req));
    }

    @PutMapping("/api/products/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ProductRequest req) {
        return ResponseEntity.ok(productService.update(id, req));
    }

    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/categories")
    public List<Category> getCategories() {
        return productService.getAllCategories();
    }

    @PostMapping("/api/categories")
    public ResponseEntity<Category> createCategory(@RequestParam String name) {
        return ResponseEntity.ok(productService.createCategory(name));
    }
}