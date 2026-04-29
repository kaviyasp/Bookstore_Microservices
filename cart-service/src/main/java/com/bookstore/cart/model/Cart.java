package com.bookstore.cart.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.math.BigDecimal;
import java.util.*;

@RedisHash("cart")
@Data @NoArgsConstructor @AllArgsConstructor
public class Cart {
    @Id
    private String userId;
    private List<CartItem> items = new ArrayList<>();
    private BigDecimal totalAmount = BigDecimal.ZERO;
}