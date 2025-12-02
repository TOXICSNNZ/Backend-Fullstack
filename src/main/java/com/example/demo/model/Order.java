package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @ManyToOne(optional = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @Column(nullable = false)
    private Integer subtotal;

    @Column(nullable = false)
    private Integer iva;

    @Column(nullable = false)
    private Integer shippingCost;

    @Column(nullable = false)
    private Integer total;

    private String status;

    private LocalDateTime createdAt;
}
