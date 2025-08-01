package com.github.leo_proger.blog.service;


import com.github.leo_proger.blog.exception.PostNotFoundException;
import com.github.leo_proger.blog.model.Post;
import com.github.leo_proger.blog.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PostServiceTest {

	@Autowired private PostRepository postRepository;

	@Autowired private PostService postService;

	@Test
	void deleteById_Success() {
		// Given
		String postText = "Some post text";

		Post post = new Post();
		post.setText(postText);

		postRepository.save(post);
		Long postID = post.getId();

		// When
		postService.deleteById(postID);

		// Then
		assertFalse(postRepository.existsById(postID));
	}

	@Test
	void deleteById_PostDoesNotExist() {
		assertThrows(PostNotFoundException.class, () -> postService.deleteById(1L));
		assertThrows(PostNotFoundException.class, () -> postService.deleteById(8712638L));
	}

}
