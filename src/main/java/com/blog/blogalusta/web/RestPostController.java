package com.blog.blogalusta.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.security.core.Authentication;

import com.blog.blogalusta.domain.BlogPost;
import com.blog.blogalusta.domain.PostService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class RestPostController {

    private final PostService postService;

    public RestPostController(PostService postService) {
        this.postService = postService;
    }

    // Hae kaikki postaukset
    @GetMapping
    public ResponseEntity<List<BlogPost>> getAllPosts() {
        List<BlogPost> posts = postService.findAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Hae yksi postaus
    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getPostById(@PathVariable Long id) {
        Optional<BlogPost> post = postService.findById(id);
        return post.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Lisää uusi postaus
    @PostMapping
    public ResponseEntity<BlogPost> createPost(@RequestBody BlogPost post) {
        BlogPost savedPost = postService.savePost(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    // Editoi postausta
    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updatePost(@PathVariable Long id, @RequestBody BlogPost post,
            Authentication authentication) {
        String editorUsername = authentication.getName();
        String newUsername = post.getNewUsername();
        try {
            BlogPost updatedPost = postService.updatePost(id, post, editorUsername, newUsername);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Poista postaus
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        return postService.findById(id).map(post -> {
            postService.deletePost(post);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
