package com.github.leo_proger.blog.configs;

import com.github.leo_proger.blog.enums.UserRole;
import com.github.leo_proger.blog.models.Post;
import com.github.leo_proger.blog.models.User;
import com.github.leo_proger.blog.repositories.PostRepository;
import com.github.leo_proger.blog.repositories.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class AppConfig {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final PasswordEncoder passwordEncoder;

    public AppConfig(UserRepository userRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;

        this.passwordEncoder = passwordEncoder;
    }

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        if (!userRepository.existsByUsername("leo_proger")) {
            User admin = new User();
            admin.setUsername("leo_proger");
            admin.setPassword(passwordEncoder.encode("12345"));
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
            System.out.println("admin created");
        }

        if (!userRepository.existsByUsername("test_user")) {
            User testUser = new User();
            testUser.setUsername("test_user");
            testUser.setPassword(passwordEncoder.encode("12345"));
            testUser.setRole(UserRole.USER);
            userRepository.save(testUser);
            System.out.println("test user created");
        }

        if (!postRepository.existsById(1L)) {
            Post post = new Post();
            post.setText("One two three four aboba. Test text. I'm implementing like feat");
            postRepository.save(post);
            System.out.println("Test post created");
        }
    }
}
