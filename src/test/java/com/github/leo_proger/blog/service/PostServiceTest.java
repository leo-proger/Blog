package com.github.leo_proger.blog.service;


import com.github.leo_proger.blog.exception.PostNotFoundException;
import com.github.leo_proger.blog.exception.UserNotFoundException;
import com.github.leo_proger.blog.model.Post;
import com.github.leo_proger.blog.model.PostLike;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.repository.PostLikeRepository;
import com.github.leo_proger.blog.repository.PostRepository;
import com.github.leo_proger.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PostServiceTest {

	@Autowired private PostRepository postRepository;

	@Autowired private PostService postServiceAutowired;

	private PostService postService;

	@Mock private PostRepository postRepositoryMock;

	@Mock private UserRepository userRepositoryMock;

	@Mock private PostLikeRepository postLikeRepositoryMock;

	@BeforeEach
	void setUp() {
		postService = new PostService(postRepositoryMock, userRepositoryMock, postLikeRepositoryMock);
	}

	@Test
	void deleteById_Success() {
		// Given
		String postText = "Some post text";

		Post post = new Post();
		post.setText(postText);

		postRepository.save(post);
		Long postID = post.getId();

		// When
		postServiceAutowired.deleteById(postID);

		// Then
		assertFalse(postRepository.existsById(postID));
	}

	@Test
	void deleteById_PostDoesNotExist() {
		assertThrows(PostNotFoundException.class, () -> postServiceAutowired.deleteById(1L));
		assertThrows(PostNotFoundException.class, () -> postServiceAutowired.deleteById(8712638L));
	}

	@Test
	void addLikeToPost_Success() {
		// Given
		long postID = 1;
		long userID = 2;

		Post post = new Post();
		post.setId(postID);

		User user = new User();
		user.setId(userID);

		// Mocks
		when(postRepositoryMock.findById(postID)).thenReturn(Optional.of(post));
		when(userRepositoryMock.findById(userID)).thenReturn(Optional.of(user));
		when(postLikeRepositoryMock.findPostLikeByUserAndPost(user, post)).thenReturn(Optional.empty());

		// When
		String result = postService.likePost(postID, userID);

		// Then
		assertEquals("ADD", result);

		ArgumentCaptor<PostLike> postLikeArgumentCaptor = ArgumentCaptor.forClass(PostLike.class);
		verify(postLikeRepositoryMock).save(postLikeArgumentCaptor.capture());

		PostLike postLike = postLikeArgumentCaptor.getValue();

		assertEquals(user, postLike.getUser());
		assertEquals(post, postLike.getPost());

		verify(postLikeRepositoryMock, never()).delete(any(PostLike.class));
	}

	@Test
	void removeLikeToPost_Success() {
		// Given
		long postID = 1;
		long userID = 2;
		long postLikeID = 3;

		Post post = new Post();
		post.setId(postID);

		User user = new User();
		user.setId(userID);

		PostLike postLike = new PostLike();
		postLike.setId(postLikeID);
		postLike.setUser(user);
		postLike.setPost(post);

		// Mocks
		when(postRepositoryMock.findById(postID)).thenReturn(Optional.of(post));
		when(userRepositoryMock.findById(userID)).thenReturn(Optional.of(user));
		when(postLikeRepositoryMock.findPostLikeByUserAndPost(user, post)).thenReturn(Optional.of(postLike));

		// When
		String result = postService.likePost(postID, userID);

		// Then
		assertEquals("REMOVE", result);

		ArgumentCaptor<PostLike> postLikeArgumentCaptor = ArgumentCaptor.forClass(PostLike.class);
		verify(postLikeRepositoryMock).delete(postLikeArgumentCaptor.capture());

		PostLike deletedPostLike = postLikeArgumentCaptor.getValue();

		assertEquals(user, deletedPostLike.getUser());
		assertEquals(post, deletedPostLike.getPost());

		verify(postLikeRepositoryMock, never()).save(any(PostLike.class));
	}

	@Test
	void likePost_PostNotFound() {
		when(postRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
		when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(new User()));

		assertThrows(PostNotFoundException.class, () -> postService.likePost(1L, 1L));
	}

	@Test
	void likePost_UserNotFound() {
		when(postRepositoryMock.findById(anyLong())).thenReturn(Optional.of(new Post()));
		when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> postService.likePost(1L, 1L));
	}

}
