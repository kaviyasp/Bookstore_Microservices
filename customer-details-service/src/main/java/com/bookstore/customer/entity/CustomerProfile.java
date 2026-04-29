package com.bookstore.customer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "customer_profiles")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String fullName;
    private String phone;
}