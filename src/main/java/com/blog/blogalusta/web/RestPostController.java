package com.blog.blogalusta.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blogalusta.domain.BlogPost;
import com.blog.blogalusta.domain.PostRepository;

@RestController
@RequestMapping("/api")
public class RestPostController {

    @Autowired
    PostRepository pRepository;

    @GetMapping("/posts")
    public Iterable<BlogPost> getPosts() {
        return pRepository.findAll();
    }

}
