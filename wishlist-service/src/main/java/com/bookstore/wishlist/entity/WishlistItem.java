package com.bookstore.wishlist.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "wishlist_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","product_id"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class WishlistItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long productId;
    private String productTitle;
}