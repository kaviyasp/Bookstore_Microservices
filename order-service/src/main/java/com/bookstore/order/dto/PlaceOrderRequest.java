package com.bookstore.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PlaceOrderRequest {
    private Long userId;
    private String userEmail;
    private List<OrderItemDto> items;
    private BigDecimal totalAmount;

    @Data
    public static class OrderItemDto {
        private Long productId;
        private String productTitle;
        private int quantity;
        private BigDecimal unitPrice;
    }
}