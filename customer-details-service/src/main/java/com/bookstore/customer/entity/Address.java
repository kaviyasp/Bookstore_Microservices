package com.bookstore.customer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "addresses")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private boolean isDefault;
}