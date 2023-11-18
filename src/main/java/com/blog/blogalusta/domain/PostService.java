package com.blog.blogalusta.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PostService {

    private final PostRepository pRepository;
    private final UserRepository uRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.pRepository = postRepository;
        this.uRepository = userRepository;
    }

    public Optional<BlogPost> findById(Long id) {
        return pRepository.findById(id);
    }

    public List<BlogPost> findAllPosts() {
        return StreamSupport.stream(pRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public BlogPost savePost(BlogPost post) {
        return pRepository.save(post);
    }

    // Mukana checkki jossa katsotaan onko editori admin. Jos on niin syöttää uuden
    // usernamen ja jos jättää tyhjäksi niin tulee alkuperäinen username
    public BlogPost updatePost(Long id, BlogPost updatedPost, String editorUsername, String newUsername) {
        return findById(id).map(originalPost -> {
            BlogUser editor = uRepository.findByUsername(editorUsername);
            if (editor.getUsername().equals(originalPost.getUser().getUsername())
                    || editor.getRole().equals("ROLE_ADMIN")) {
                if (editor.getRole().equals("ROLE_ADMIN") && newUsername != null && !newUsername.isEmpty()) {
                    BlogUser newUser = uRepository.findByUsername(newUsername);
                    if (newUser != null) {
                        updatedPost.setUser(newUser);
                    } else {
                        throw new SecurityException("New user not found.");
                    }
                } else {
                    updatedPost.setUser(originalPost.getUser());
                }
                return savePost(updatedPost);
            } else {
                throw new SecurityException("You do not have permission to edit this post.");
            }
        }).orElseThrow(() -> new IllegalArgumentException("No post found with id: " + id));
    }

    public void deletePost(BlogPost post) {
        pRepository.delete(post);
    }
}
