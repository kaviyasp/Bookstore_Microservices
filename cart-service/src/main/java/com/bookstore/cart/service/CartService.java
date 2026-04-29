package com.bookstore.cart.service;

import com.bookstore.cart.dto.AddItemRequest;
import com.bookstore.cart.model.*;
import com.bookstore.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart getCart(String userId) {
        return cartRepository.findById(userId).orElse(new Cart(userId, new java.util.ArrayList<>(), BigDecimal.ZERO));
    }

    public Cart addItem(String userId, AddItemRequest req) {
        Cart cart = getCart(userId);
        cart.getItems().stream()
                .filter(i -> i.getProductId().equals(req.getProductId()))
                .findFirst()
                .ifPresentOrElse(
                        i -> i.setQuantity(i.getQuantity() + req.getQuantity()),
                        () -> cart.getItems().add(new CartItem(
                                req.getProductId(), req.getProductTitle(),
                                req.getQuantity(), req.getUnitPrice()))
                );
        recalculate(cart);
        return cartRepository.save(cart);
    }

    public Cart updateItem(String userId, Long productId, int quantity) {
        Cart cart = getCart(userId);
        cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .ifPresent(i -> i.setQuantity(quantity));
        recalculate(cart);
        return cartRepository.save(cart);
    }

    public Cart removeItem(String userId, Long productId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        recalculate(cart);
        return cartRepository.save(cart);
    }

    public void clearCart(String userId) {
        cartRepository.deleteById(userId);
    }

    private void recalculate(Cart cart) {
        cart.setTotalAmount(cart.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}