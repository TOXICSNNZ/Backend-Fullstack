package com.example.demo.controller;

import com.example.demo.model.BlogPost;
import com.example.demo.service.BlogPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
@Tag(name = "Blog", description = "Gestión de posts de blog (alimentación saludable y sostenibilidad)")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @GetMapping
    @Operation(summary = "Ver todas las entradas del blog")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<BlogPost> getAllPosts() {
        return blogPostService.getAllPosts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver una entrada específica del blog")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BlogPost getPostById(@PathVariable Long id) {
        return blogPostService.getPostById(id);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva entrada de blog (solo ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public BlogPost createPost(@RequestBody BlogPost post) {
        return blogPostService.createPost(post);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar una entrada de blog (solo ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public BlogPost updatePost(@PathVariable Long id, @RequestBody BlogPost post) {
        return blogPostService.updatePost(id, post);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una entrada de blog (solo ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePost(@PathVariable Long id) {
        blogPostService.deletePost(id);
    }
}
