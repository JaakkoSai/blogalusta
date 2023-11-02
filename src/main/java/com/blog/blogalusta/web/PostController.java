package com.blog.blogalusta.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.blog.blogalusta.domain.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.blog.blogalusta.domain.BlogPost;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Add a new post form
    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("post", new BlogPost());
        return "post-add";
    }

    // Save a new post
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addPost(@Valid @ModelAttribute("post") BlogPost post, BindingResult result) {
        if (result.hasErrors()) {
            return "post-add";
        }
        postService.savePost(post);
        return "redirect:/posts";
    }

    // Edit a post form
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        BlogPost post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        return "post-update";
    }

    // Update a post
    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updatePost(@PathVariable("id") long id, @Valid @ModelAttribute("post") BlogPost post,
            BindingResult result) {
        if (result.hasErrors()) {
            post.setId(id);
            return "post-update";
        }

        postService.savePost(post);
        return "redirect:/posts";
    }

    // Delete a post
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePost(@PathVariable("id") long id, Model model) {
        BlogPost post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        postService.deletePost(post);
        return "redirect:/posts";
    }

    // Get all posts
    @GetMapping
    public String getAllPosts(Model model) {
        model.addAttribute("posts", postService.findAllPosts());
        return "posts";
    }
}
