package com.bookstore.notification.service;

import com.bookstore.notification.event.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void onOrderEvent(OrderEvent event) {
        switch (event.getType()) {
            case "ORDER_PLACED" -> log.info(
                    "📧 Sending order confirmation to {} for order #{}",
                    event.getUserEmail(), event.getOrderId());
            case "ORDER_SHIPPED" -> log.info(
                    "📦 Sending shipping update to {} for order #{}",
                    event.getUserEmail(), event.getOrderId());
            case "ORDER_DELIVERED" -> log.info(
                    "✅ Sending delivery confirmation to {} for order #{}",
                    event.getUserEmail(), event.getOrderId());
            default -> log.info("Event received: {}", event.getType());
        }
    }
}