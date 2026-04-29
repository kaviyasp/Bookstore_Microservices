package com.bookstore.wishlist.controller;

import com.bookstore.wishlist.entity.WishlistItem;
import com.bookstore.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistRepository wishlistRepo;

    @GetMapping
    public List<WishlistItem> getWishlist(@RequestHeader("userId") Long userId) {
        return wishlistRepo.findByUserId(userId);
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<WishlistItem> add(@RequestHeader("userId") Long userId,
                                            @PathVariable Long productId,
                                            @RequestParam(required = false) String title) {
        WishlistItem item = WishlistItem.builder()
                .userId(userId).productId(productId).productTitle(title).build();
        return ResponseEntity.ok(wishlistRepo.save(item));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> remove(@RequestHeader("userId") Long userId,
                                       @PathVariable Long productId) {
        wishlistRepo.findByUserIdAndProductId(userId, productId)
                .ifPresent(wishlistRepo::delete);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clear(@RequestHeader("userId") Long userId) {
        wishlistRepo.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}