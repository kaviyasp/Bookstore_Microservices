package com.bookstore.product.service;

import com.bookstore.product.dto.*;
import com.bookstore.product.entity.*;
import com.bookstore.product.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    private ProductResponse toResponse(Product p) {
        ProductResponse r = new ProductResponse();
        r.setId(p.getId());
        r.setTitle(p.getTitle());
        r.setAuthor(p.getAuthor());
        r.setIsbn(p.getIsbn());
        r.setPrice(p.getPrice());
        r.setStockQuantity(p.getStockQuantity());
        r.setImageUrl(p.getImageUrl());
        if (p.getCategory() != null) r.setCategoryName(p.getCategory().getName());
        return r;
    }

    public Page<ProductResponse> getAll(int page, int size) {
        return productRepo.findAll(PageRequest.of(page, size)).map(this::toResponse);
    }

    public ProductResponse getById(Long id) {
        return toResponse(productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found")));
    }

    public List<ProductResponse> search(String q) {
        return productRepo
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(q, q)
                .stream().map(this::toResponse).toList();
    }

    public List<ProductResponse> getByCategory(Long categoryId) {
        return productRepo.findByCategoryId(categoryId)
                .stream().map(this::toResponse).toList();
    }

    public ProductResponse create(ProductRequest req) {
        Category category = req.getCategoryId() != null
                ? categoryRepo.findById(req.getCategoryId()).orElse(null) : null;
        Product p = Product.builder()
                .title(req.getTitle()).author(req.getAuthor())
                .isbn(req.getIsbn()).price(req.getPrice())
                .stockQuantity(req.getStockQuantity())
                .imageUrl(req.getImageUrl()).category(category).build();
        return toResponse(productRepo.save(p));
    }

    public ProductResponse update(Long id, ProductRequest req) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        p.setTitle(req.getTitle()); p.setAuthor(req.getAuthor());
        p.setPrice(req.getPrice()); p.setStockQuantity(req.getStockQuantity());
        p.setImageUrl(req.getImageUrl());
        if (req.getCategoryId() != null)
            p.setCategory(categoryRepo.findById(req.getCategoryId()).orElse(null));
        return toResponse(productRepo.save(p));
    }

    public void delete(Long id) { productRepo.deleteById(id); }

    public List<Category> getAllCategories() { return categoryRepo.findAll(); }

    public Category createCategory(String name) {
        return categoryRepo.save(Category.builder().name(name).build());
    }
}