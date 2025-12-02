package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderService.OrderItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "HuertoHogar Orders API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Crear pedido a partir del carrito")
    @PreAuthorize("hasRole('USER')")
    public Order createOrder(@RequestBody List<OrderItemRequest> items,
                             Authentication authentication) {

        String username = authentication.getName();
        return orderService.createOrder(username, items);
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener pedidos del usuario actual")
    @PreAuthorize("hasRole('USER')")
    public List<Order> getMyOrders(Authentication authentication) {
        String username = authentication.getName();
        return orderService.getOrdersByUser(username);
    }

    @GetMapping
    @Operation(summary = "Listar todos los pedidos (Solo ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> getAll() {
        return orderService.getAllOrders();
    }
}
