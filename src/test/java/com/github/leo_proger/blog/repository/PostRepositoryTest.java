package com.github.leo_proger.blog.repository;


import com.github.leo_proger.blog.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void findAllByOrderByCreatedAtDesc() {
        // Given
        Post postOld = new Post();
        postOld.setText("Old post");
        postOld.setCreatedAt(LocalDateTime.now().minusDays(1));

        Post postNew = new Post();
        postNew.setText("New post");
        postNew.setCreatedAt(LocalDateTime.now());

        postRepository.save(postOld);
        postRepository.save(postNew);

        // When
        List<Post> result = postRepository.findAllByOrderByCreatedAtDesc();

        // Then
        assertEquals(2, result.size());
        assertEquals("New post", result.getFirst().getText());
        assertEquals("Old post", result.get(1).getText());
    }
}