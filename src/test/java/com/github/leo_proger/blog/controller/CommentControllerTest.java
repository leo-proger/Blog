package com.github.leo_proger.blog.controller;


import com.github.leo_proger.blog.dto.CommentDTO;
import com.github.leo_proger.blog.service.CommentService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

	@Mock
	private CommentService commentService;

	@Mock
	private Validator validator;

	@InjectMocks
	private CommentController controller;

	@Test
	void createComment_Success() {
		// Given
		String commentText = "Some text";
		Long postID = 25L;
		String username = "leo_proger";

		CommentDTO commentDTO = new CommentDTO(username, postID, commentText);

		when(validator.validate(any(CommentDTO.class))).thenReturn(Collections.emptySet());
		when(commentService.createComment(username, postID, commentText)).thenReturn(commentDTO);

		// When
		ResponseEntity<?> response = controller.createComment(commentText, postID, () -> username);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());

		@SuppressWarnings("unchecked")
		Map<String, Object> body = (Map<String, Object>) response.getBody();

		assertNotNull(body);
		assertEquals(1, body.size());
		assertTrue(body.containsKey("Comment"));
		assertSame(commentDTO, body.get("Comment"));

		verify(validator, times(1)).validate(any(CommentDTO.class));
		verify(commentService, times(1)).createComment(anyString(), anyLong(), anyString());
	}

	@Test
	void createComment_Unauthorized() {
		// Given
		Long postID = 1312L;
		String commentText = "Some text for a comment";

		// When
		ResponseEntity<?> response = controller.createComment(commentText, postID, null);

		// Then
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertNull(response.getBody());
		verifyNoInteractions(validator, commentService);
	}

	@Test
	void createComment_Violation() {
		// Given
		String commentText = "";
		Long postID = 2L;
		String username = "leo_proger";
		String errorText = "Text can't be empty";
		String errorFiled = "text";

		Set<ConstraintViolation<CommentDTO>> violations = new HashSet<>();

		ConstraintViolation<CommentDTO> violation = mockViolation(errorFiled, errorText);
		violations.add(violation);

		when(validator.validate(any(CommentDTO.class))).thenReturn(violations);

		// When
		ResponseEntity<?> response = controller.createComment(commentText, postID, () -> username);

		// Then
		@SuppressWarnings("unchecked")
		Map<String, Object> body = (Map<String, Object>) response.getBody();

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(body);
		assertEquals(1, body.size());
		assertEquals(errorText, body.get(errorFiled));

		verify(validator, times(1)).validate(any(CommentDTO.class));
		verifyNoInteractions(commentService);
	}

	@Test
	void createComment_MultipleViolation() {
		// Given
		String username = "";
		Long postID = 2L;
		String commentText = "";

		String errorFiled1 = "authorName";
		String errorText1 = "Author can't be empty";

		String errorFiled2 = "postID";
		String errorText2 = "Post ID can't be negative or zero";

		String errorFiled3 = "text";
		String errorText3 = "Text can't be empty";

		Set<ConstraintViolation<CommentDTO>> violations = Set.of(
				mockViolation(errorFiled1, errorText1),
				mockViolation(errorFiled2, errorText2),
				mockViolation(errorFiled3, errorText3)
		);

		when(validator.validate(any(CommentDTO.class))).thenReturn(violations);

		// When
		ResponseEntity<?> response = controller.createComment(commentText, postID, () -> username);

		// Then
		@SuppressWarnings("unchecked")
		Map<String, Object> body = (Map<String, Object>) response.getBody();

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(body);
		assertEquals(3, body.size());

		assertEquals(errorText1, body.get(errorFiled1));
		assertEquals(errorText2, body.get(errorFiled2));
		assertEquals(errorText3, body.get(errorFiled3));

		verify(validator, times(1)).validate(any(CommentDTO.class));
		verifyNoInteractions(commentService);
	}

	@Test
	void createComment_SpecialChars() {
		// Given
		String commentText = "It's a comment with special chars: !@#$%^&*()-_=+`~\"'[]{}:;.,<>/?|\\";
		Long postID = 12L;
		String username = "leo_proger";

		CommentDTO commentDTO = new CommentDTO(username, postID, commentText);

		when(commentService.createComment(username, postID, commentText)).thenReturn(commentDTO);

		// When
		ResponseEntity<?> response = controller.createComment(commentText, postID, () -> username);

		// Then
		@SuppressWarnings("unchecked")
		Map<String, Object> body = (Map<String, Object>) response.getBody();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(body);
		assertEquals(1, body.size());
		assertSame(commentDTO, body.get("Comment"));

		// mockMvc.perform(
		// 		       post("/posts/comments/create/{postID}", postID)
		// 				       .param("text", commentText)
		// 				       .principal(() -> username)
		// 				       .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		//        )
		//        .andExpect(status().isOk())
		//        .andExpect(jsonPath("$.text").value(commentText));
	}

	// Utils

	private ConstraintViolation<CommentDTO> mockViolation(String field, String errorText) {
		@SuppressWarnings("unchecked")
		ConstraintViolation<CommentDTO> violation = mock(ConstraintViolation.class);
		Path propertyPath = mock(Path.class);

		when(propertyPath.toString()).thenReturn(field);
		when(violation.getPropertyPath()).thenReturn(propertyPath);
		when(violation.getMessage()).thenReturn(errorText);

		return violation;
	}

}