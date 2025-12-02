package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    private Product product;

    @ManyToOne(optional = false)
    private Order order;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;
}
