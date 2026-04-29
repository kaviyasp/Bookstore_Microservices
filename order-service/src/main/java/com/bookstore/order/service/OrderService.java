package com.bookstore.order.service;

import com.bookstore.order.dto.PlaceOrderRequest;
import com.bookstore.order.entity.*;
import com.bookstore.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderEventPublisher eventPublisher;

    public Order placeOrder(PlaceOrderRequest req) {
        Order order = Order.builder()
                .userId(req.getUserId())
                .userEmail(req.getUserEmail())
                .status(OrderStatus.PENDING)
                .totalAmount(req.getTotalAmount())
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> items = req.getItems().stream()
                .map(i -> OrderItem.builder()
                        .order(order)
                        .productId(i.getProductId())
                        .productTitle(i.getProductTitle())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .build())
                .toList();
        order.setItems(items);

        Order saved = orderRepo.save(order);
        eventPublisher.publishOrderPlaced(saved);
        return saved;
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    public Order getOrder(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order updateStatus(Long id, String status) {
        Order order = getOrder(id);
        order.setStatus(OrderStatus.valueOf(status));
        Order saved = orderRepo.save(order);
        eventPublisher.publishStatusChanged(saved);
        return saved;
    }

    public Order cancelOrder(Long id) {
        Order order = getOrder(id);
        if (order.getStatus() != OrderStatus.PENDING)
            throw new RuntimeException("Only PENDING orders can be cancelled");
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepo.save(order);
    }

    public List<Order> getAllOrders() { return orderRepo.findAll(); }
}