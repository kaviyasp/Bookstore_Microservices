package com.bookstore.cart.controller;

import com.bookstore.cart.dto.AddItemRequest;
import com.bookstore.cart.model.Cart;
import com.bookstore.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestHeader("userId") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addItem(@RequestHeader("userId") String userId,
                                        @RequestBody AddItemRequest req) {
        return ResponseEntity.ok(cartService.addItem(userId, req));
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateItem(@RequestHeader("userId") String userId,
                                           @RequestParam Long productId,
                                           @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateItem(userId, productId, quantity));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeItem(@RequestHeader("userId") String userId,
                                           @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItem(userId, productId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestHeader("userId") String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    public ResponseEntity<java.math.BigDecimal> getTotal(@RequestHeader("userId") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId).getTotalAmount());
    }
}