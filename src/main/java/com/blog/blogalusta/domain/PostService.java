package com.blog.blogalusta.domain;

import com.blog.blogalusta.domain.BlogPost;
import com.blog.blogalusta.domain.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PostService {

    private final PostRepository pRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.pRepository = postRepository;
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

    public void deletePost(BlogPost post) {
        pRepository.delete(post);
    }
}
