package com.bookstore.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank private String title;
    @NotBlank private String author;
    private String isbn;
    @NotNull @Positive private BigDecimal price;
    @NotNull @Min(0) private Integer stockQuantity;
    private String imageUrl;
    private Long categoryId;
}