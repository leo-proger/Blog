package com.github.leo_proger.blog.service;


import com.github.leo_proger.blog.dto.CommentDTO;
import com.github.leo_proger.blog.exception.PostNotFoundException;
import com.github.leo_proger.blog.exception.UserNotFoundException;
import com.github.leo_proger.blog.mapper.CommentMapper;
import com.github.leo_proger.blog.model.Comment;
import com.github.leo_proger.blog.model.Post;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.repository.CommentRepository;
import com.github.leo_proger.blog.repository.PostRepository;
import com.github.leo_proger.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	private CommentService commentService;

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PostRepository postRepository;

	@Mock
	private CommentMapper commentMapper;

	private final String authorName = "leo_proger";
	private final Long postID = 2L;
	private final String commentText = "Test text for comment";

	@BeforeEach
	void setUp() {
		commentService = new CommentService(commentRepository, userRepository, postRepository, commentMapper);
	}

	@Test
	void createComment_Success() {
		// Given
		User user = new User();
		user.setUsername(authorName);

		Post post = new Post();
		post.setId(postID);

		Comment comment = new Comment();
		comment.setId(3L);
		comment.setText(commentText);
		comment.setAuthor(user);
		comment.setPost(post);

		CommentDTO commentDTO = new CommentDTO(authorName, postID, commentText);

		// Mocks
		when(userRepository.findByUsername(authorName)).thenReturn(Optional.of(user));
		when(postRepository.findById(postID)).thenReturn(Optional.of(post));
		when(commentRepository.save(any(Comment.class))).thenReturn(comment);
		when(commentMapper.toDTO(comment)).thenReturn(commentDTO);

		// When
		CommentDTO resultingCommentDTO = commentService.createComment(authorName, postID, commentText);

		// Then
		assertNotNull(resultingCommentDTO);
		assertEquals(authorName, resultingCommentDTO.getAuthorName());
		assertEquals(postID, resultingCommentDTO.getPostID());
		assertEquals(commentText, resultingCommentDTO.getText());

		verify(commentRepository).save(any(Comment.class));
	}

	@Test
	void createComment_UserNotFound() {
		when(userRepository.findByUsername(anyString())).thenThrow(UserNotFoundException.class);

		assertThrows(UserNotFoundException.class, () -> commentService.createComment(authorName, postID, commentText));

		verify(postRepository, never()).findById(anyLong());
		verify(commentRepository, never()).save(any());
	}

	@Test
	void createComment_PostNotFound() {
		User user = new User();

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(postRepository.findById(anyLong())).thenThrow(PostNotFoundException.class);

		assertThrows(PostNotFoundException.class, () -> commentService.createComment(authorName, postID, commentText));

		verify(commentRepository, never()).save(any());
	}

}