package com.blog.blogalusta.domain;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<BlogUser, Long> {

}
