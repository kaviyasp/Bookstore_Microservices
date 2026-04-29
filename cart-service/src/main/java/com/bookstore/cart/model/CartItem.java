package com.bookstore.cart.model;

import lombok.*;
import java.math.BigDecimal;
import java.io.Serializable;

@Data @NoArgsConstructor @AllArgsConstructor
public class CartItem implements Serializable {
    private Long productId;
    private String productTitle;
    private int quantity;
    private BigDecimal unitPrice;
}