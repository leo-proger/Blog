package com.github.leo_proger.blog.config;

import com.github.leo_proger.blog.enums.UserRole;
import com.github.leo_proger.blog.model.Comment;
import com.github.leo_proger.blog.model.Post;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.repository.CommentRepository;
import com.github.leo_proger.blog.repository.PostRepository;
import com.github.leo_proger.blog.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


@Configuration
public class AppConfig {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final PasswordEncoder passwordEncoder;

    public AppConfig(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;

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

        if (!commentRepository.existsById(1L)) {
            Optional<User> author = userRepository.findById(1L);
            Optional<Post> post = postRepository.findById(1L);

            if (author.isPresent() && post.isPresent()) {
                Comment comment = new Comment();
                comment.setAuthor(author.get());
                comment.setPost(post.get());
                comment.setText("This is a test comment");

                commentRepository.save(comment);
                System.out.println("Test comment created");
            } else {
                System.out.println("Cannot create a comment");
            }
        }
    }
}
