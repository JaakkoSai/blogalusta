package com.blog.blogalusta.domain;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<BlogPost, Long> {

}
