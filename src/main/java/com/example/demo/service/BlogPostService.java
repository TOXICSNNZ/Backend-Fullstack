package com.example.demo.service;

import com.example.demo.model.BlogPost;
import com.example.demo.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    public List<BlogPost> getAllPosts() {
        return blogPostRepository.findAll();
    }

    public BlogPost getPostById(Long id) {
        return blogPostRepository.findById(id).orElse(null);
    }

    public BlogPost createPost(BlogPost post) {
        if (post.getCreatedAt() == null) {
            post.setCreatedAt(LocalDateTime.now());
        }
        return blogPostRepository.save(post);
    }

    public BlogPost updatePost(Long id, BlogPost post) {
        BlogPost existing = blogPostRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        existing.setTitle(post.getTitle());
        existing.setCategory(post.getCategory());
        existing.setSummary(post.getSummary());
        existing.setContent(post.getContent());
        existing.setImageUrl(post.getImageUrl());

        return blogPostRepository.save(existing);
    }

    public void deletePost(Long id) {
        blogPostRepository.deleteById(id);
    }
}
