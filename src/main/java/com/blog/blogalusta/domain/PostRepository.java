package com.blog.blogalusta.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<BlogPost, Long> {
    List<BlogPost> findByUser(BlogUser user);

}
