package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "HuertoHogar Products API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Product getById(@PathVariable Long id) {
        return productService.getById(id).orElse(null);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Obtener producto por código")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Product getByCode(@PathVariable String code) {
        return productService.getByCode(code).orElse(null);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto (Solo ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public Product create(@RequestBody Product product) {
        return productService.save(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto (Solo ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return productService.getById(id)
                .map(existing -> {
                    existing.setCode(product.getCode());
                    existing.setName(product.getName());
                    existing.setCategory(product.getCategory());
                    existing.setPrice(product.getPrice());
                    existing.setStock(product.getStock());
                    existing.setDescription(product.getDescription());
                    existing.setImageUrl(product.getImageUrl());
                    return productService.save(existing);
                })
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto (Solo ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
