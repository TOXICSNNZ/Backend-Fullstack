package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Order createOrder(String username, List<OrderItemRequest> itemsReq) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        int subtotal = 0;

        Order order = Order.builder()
                .orderNumber("HH-" + UUID.randomUUID())
                .user(user)
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();

        order = orderRepository.save(order);

        for (OrderItemRequest req : itemsReq) {
            Product product = productRepository.findByCode(req.getCode())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + req.getCode()));

            int linePrice = product.getPrice();
            subtotal += linePrice * req.getQuantity();

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(req.getQuantity())
                    .price(linePrice)
                    .build();

            orderItemRepository.save(item);
        }

        int iva = (int) Math.round(subtotal * 0.19);
        int shipping = 3000; // por ejemplo
        int total = subtotal + iva + shipping;

        order.setSubtotal(subtotal);
        order.setIva(iva);
        order.setShippingCost(shipping);
        order.setTotal(total);

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return orderRepository.findByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public static class OrderItemRequest {
        private String code;
        private Integer quantity;

        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public Integer getQuantity() {
            return quantity;
        }
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
