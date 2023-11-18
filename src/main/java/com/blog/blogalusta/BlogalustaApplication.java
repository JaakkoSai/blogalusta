package com.blog.blogalusta;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blog.blogalusta.domain.PostRepository;
import com.blog.blogalusta.domain.UserRepository;
import com.blog.blogalusta.domain.BlogPost;
import com.blog.blogalusta.domain.BlogUser;

@SpringBootApplication
public class BlogalustaApplication {

	private static final Logger log = LoggerFactory.getLogger(BlogalustaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BlogalustaApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(PostRepository postRepository, UserRepository userRepository) {
		return (args) -> {
			log.info("Saving blog users and posts");

			BlogUser user1 = new BlogUser("user", "$2a$10$IXja05pQPoe0cMnjjuqrVeeYRpzQwP8G5Lxjz70EZzCCTX.bUUIgO",
					"ROLE_USER", "user@example.com", new HashSet<>());
			BlogUser user2 = new BlogUser("admin", "$2a$10$nV1cYbG62aeTqdsdDqVTh.BroH0e4WfyW0ajYqcDWB4CP3UaozEje",
					"ROLE_ADMIN", "admin@example.com", new HashSet<>());
			userRepository.save(user1);
			userRepository.save(user2);

			BlogPost post1 = new BlogPost(null, "Tervetuloa mun blogiin", "Tässä ekan userin content", user1);
			BlogPost post2 = new BlogPost(null, "Adminin blogi", "Adminin content", user2);
			postRepository.save(post1);
			postRepository.save(post2);

			log.info("Added all blog data.");
			for (BlogPost post : postRepository.findAll()) {
				log.info(post.toString());
			}
		};
	}
}
