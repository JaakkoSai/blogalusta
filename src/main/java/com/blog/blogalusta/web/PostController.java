package com.blog.blogalusta.web;

import java.util.stream.Collectors;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.blog.blogalusta.domain.PostService;
import com.blog.blogalusta.domain.SignupForm;
import com.blog.blogalusta.domain.UserRepository;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.blog.blogalusta.domain.BlogPost;
import com.blog.blogalusta.domain.BlogUser;

import jakarta.validation.Valid;

@Controller
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    @Autowired
    public PostController(PostService postService, UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    private BlogUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userRepository.findByUsername(currentUsername);
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @PostMapping("/signup")
    public String signup(@Valid SignupForm signupForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        return "redirect:/success";
    }

    @GetMapping(value = { "/index" })
    public String index() {
        return "index";
    }

    // Lisää post
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("post", new BlogPost());
        return "post-add";
    }

    // Tallenna post
    @PostMapping("/add")
    public String addPost(@Valid @ModelAttribute("post") BlogPost post, BindingResult result) {
        if (result.hasErrors()) {
            return "post-add";
        }
        post.setUser(getCurrentUser());
        postService.savePost(post);
        return "redirect:/posts";
    }

    // Editoi postaus
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        BlogPost post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ei löytynyt postausta ideellä:" + id));
        model.addAttribute("post", post);
        return "post-update";
    }

    // updatee postaus
    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable("id") long id, @Valid @ModelAttribute("post") BlogPost post,
            BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            post.setId(id);
            return "post-update";
        }

        try {
            String editorUsername = authentication.getName();
            postService.updatePost(id, post, editorUsername, post.getNewUsername());
        } catch (SecurityException e) {
            model.addAttribute("error", e.getMessage());
            return "post-update";
        }

        return "redirect:/posts";
    }

    // Poista postaus
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") long id, Model model) {
        BlogPost post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        postService.deletePost(post);
        return "redirect:/posts";
    }

    // Saa kaikki postit
    @GetMapping("/posts")
    public String getAllPosts(Model model) {
        List<BlogPost> posts = postService.findAllPosts().stream()
                .filter(post -> post.getUser() != null)
                .collect(Collectors.toList());

        model.addAttribute("posts", posts);
        return "posts";
    }

}
