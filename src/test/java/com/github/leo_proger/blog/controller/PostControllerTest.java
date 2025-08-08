package com.github.leo_proger.blog.controller;


import com.github.leo_proger.blog.model.Post;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.service.PostService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PostService postService;

	@Autowired
	private PostController controller;


	@Nested
	class CreatePost_SuccessCases {

		@Test
		void createGet_Success() throws Exception {
			mockMvc.perform(get("/posts/create"))
			       .andExpect(status().isOk())
			       .andExpect(view().name("create_post"))
			       .andExpect(model().attributeExists("postDTO"));
		}

		@Test
		void createPost_OnlyText_Success() throws Exception {
			// Given
			String postText = "Some text for a post";

			// When & Then
			mockMvc.perform(
					       post("/posts/create")
							       .param("text", postText)
			       )
			       .andExpect(status().is3xxRedirection())
			       .andExpect(redirectedUrl("/"));

			verify(postService).save(any(Post.class));
		}

		@Test
		void createPost_OnlyImageFile_Success() throws Exception {
			// Given
			MockMultipartFile file = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg", "image".getBytes());

			// When & Then
			mockMvc.perform(
					       multipart("/posts/create")
							       .file(file)
							       .contentType(MediaType.MULTIPART_FORM_DATA)
			       ).andExpect(status().is3xxRedirection())
			       .andExpect(redirectedUrl("/"));

			verify(postService).save(any(Post.class));
		}

		@Test
		void createPost_OnlyImageURL_Success() throws Exception {
			// Given
			String imageURL = "https://plus.unsplash.com/premium_photo-1664640458531-3c7cca2a9323?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";

			// When & Then
			mockMvc.perform(
					       post("/posts/create")
							       .param("imageUrl", imageURL)
							       .contentType(MediaType.IMAGE_PNG)
			       ).andExpect(status().is3xxRedirection())
			       .andExpect(redirectedUrl("/"));

			verify(postService).save(any(Post.class));
		}

		@Test
		void createPost_TextAndImageFile_Success() throws Exception {
			// Given
			String postText = "Text for a post";
			MockMultipartFile file = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg", "image".getBytes());

			// When & Then
			mockMvc.perform(
					       multipart("/posts/create")
							       .file(file)
							       .param("text", postText)
			       ).andExpect(status().is3xxRedirection())
			       .andExpect(redirectedUrl("/"));

			verify(postService).save(any(Post.class));
		}

		@Test
		void createPost_TextAndImageURL_Success() throws Exception {
			// Given
			String postText = "Text for a post";
			String imageURL = "https://plus.unsplash.com/premium_photo-1664640458531-3c7cca2a9323?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";

			// When & Then
			mockMvc.perform(
					       post("/posts/create")
							       .param("text", postText)
							       .param("imageUrl", imageURL)
			       ).andExpect(status().is3xxRedirection())
			       .andExpect(redirectedUrl("/"));

			verify(postService).save(any(Post.class));
		}

	}


	@Nested
	class CreatePost_ErrorCases {

		@Test
		void createPost_EmptyText_Error() throws Exception {
			// Given
			String postText = "";

			// When & Then
			mockMvc.perform(
					       post("/posts/create")
							       .param("text", postText)
			       )
			       .andExpect(status().isOk())
			       .andExpect(view().name("create_post"))
			       .andExpect(model().attributeHasFieldErrors("postDTO", "text"));

			verifyNoInteractions(postService);
		}

		@Test
		void createPost_TextNull_Error() throws Exception {
			// Given
			String postText = null;

			// When & Then
			mockMvc.perform(
					       post("/posts/create")
							       .param("text", postText)
			       )
			       .andExpect(status().isOk())
			       .andExpect(view().name("create_post"))
			       .andExpect(model().attributeHasFieldErrors("postDTO", "text"));

			verifyNoInteractions(postService);
		}

		@Test
		void createPost_CorruptedImageFile_IncorrectFileType_Error() throws Exception {
			// Given
			MockMultipartFile file = new MockMultipartFile("imageFile", "image.jpg", "text/plain", "image".getBytes());

			// When & Then
			mockMvc.perform(
					       multipart("/posts/create")
							       .file(file)
							       .contentType(MediaType.MULTIPART_FORM_DATA)
			       ).andExpect(status().isOk())
			       .andExpect(view().name("create_post"))
			       .andExpect(model().attributeHasFieldErrors("postDTO", "imageFile"));

			verifyNoInteractions(postService);
		}

		@Test
		void createPost_ImageURLEmpty_Error() throws Exception {
			// Given
			String imageURL = "";

			// When & Then
			mockMvc.perform(
					       post("/posts/create")
							       .param("imageUrl", imageURL)
							       .contentType(MediaType.IMAGE_PNG)
			       ).andExpect(status().isOk())
			       .andExpect(view().name("create_post"))
			       .andExpect(model().attributeHasFieldErrors("postDTO", "text"));

			verifyNoInteractions(postService);
		}

		@Test
		void createPost_ImageURLNull_Error() throws Exception {
			// Given
			String imageURL = null;

			// When & Then
			mockMvc.perform(
					       post("/posts/create")
							       .param("imageUrl", imageURL)
							       .contentType(MediaType.IMAGE_PNG)
			       ).andExpect(status().isOk())
			       .andExpect(view().name("create_post"))
			       .andExpect(model().attributeHasFieldErrors("postDTO", "text"));

			verifyNoInteractions(postService);
		}

		@Test
		void createPost_ImageURL_IncorrectFileType_Error() throws Exception {
			// Given
			String imageURL = "https://google.com";

			// When & Then
			mockMvc.perform(
					       post("/posts/create")
							       .param("imageUrl", imageURL)
							       .contentType(MediaType.IMAGE_PNG)
			       ).andExpect(status().isOk())
			       .andExpect(view().name("create_post"))
			       .andExpect(model().attributeHasFieldErrors("postDTO", "imageUrl"));

			verifyNoInteractions(postService);
		}

		@Test
		void createPost_TextAndImageFileAndImageURL_Error() throws Exception {
			// Given
			String postText = "Lorem ipsum";
			String imageURL = "https://plus.unsplash.com/premium_photo-1664640458531-3c7cca2a9323?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
			MockMultipartFile file = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg", "image".getBytes());


			// When & Then
			mockMvc.perform(
					       multipart("/posts/create")
							       .file(file)
							       .param("text", postText)
							       .param("imageUrl", imageURL)
			       )
			       .andExpect(status().isOk())
			       .andExpect(view().name("create_post"))
			       .andExpect(model().attributeHasFieldErrors("postDTO", "imageUrl"));

			verifyNoInteractions(postService);
		}

		@Test
		void createPost_NoParams_Error() throws Exception {
			// Given
			String postText = null;
			String imageURL = null;
			MockMultipartFile file = new MockMultipartFile("imageFile", "", "", "".getBytes());


			// When & Then
			mockMvc.perform(
					       multipart("/posts/create")
							       .file(file)
							       .param("text", postText)
							       .param("imageUrl", imageURL)
			       )
			       .andExpect(status().isOk())
			       .andExpect(view().name("create_post"))
			       .andExpect(model().attributeHasFieldErrors("postDTO", "text"));

			verifyNoInteractions(postService);
		}

		@Test
		void createPost_ImageFileAndImageURL_Error() throws Exception {
			// Given
			String imageURL = "https://plus.unsplash.com/premium_photo-1664640458531-3c7cca2a9323?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
			MockMultipartFile file = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg", "image".getBytes());


			// When & Then
			mockMvc.perform(
					       multipart("/posts/create")
							       .file(file)
							       .param("imageUrl", imageURL)
			       )
			       .andExpect(status().isOk())
			       .andExpect(view().name("create_post"))
			       .andExpect(model().attributeHasFieldErrors("postDTO", "imageUrl"));

			verifyNoInteractions(postService);
		}

	}

	@Test
	void deletePost_Success() throws Exception {
		// Given
		Long postID = 141L;

		// When & Then
		mockMvc.perform(post("/posts/delete/{postID}", postID))
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/"));

		verify(postService).deleteById(anyLong());
		verify(postService, never()).save(any(Post.class));
	}

	@Test
	void addLikePost_Success() {
		// Given
		Long userID = 798L;
		Long postID = 412L;

		User user = new User();
		user.setId(userID);

		when(postService.likePost(postID, userID)).thenReturn("ADD");

		// When
		ResponseEntity<?> response = controller.likePost(postID, user);

		// Then
		@SuppressWarnings("unchecked")
		Map<String, Object> body = (Map<String, Object>) response.getBody();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(body);
		assertEquals("ADD", body.get("Action"));

		verify(postService).likePost(anyLong(), anyLong());
	}

	@Test
	void removeLikePost_Success() {
		// Given
		Long userID = 798L;
		Long postID = 412L;

		User user = new User();
		user.setId(userID);

		when(postService.likePost(postID, userID)).thenReturn("REMOVE");

		// When
		ResponseEntity<?> response = controller.likePost(postID, user);

		// Then
		@SuppressWarnings("unchecked")
		Map<String, Object> body = (Map<String, Object>) response.getBody();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(body);
		assertEquals("REMOVE", body.get("Action"));

		verify(postService).likePost(anyLong(), anyLong());
	}

	@Test
	void likePost_UserNull_Error() {
		// Given
		Long postID = 7271L;

		// When
		ResponseEntity<?> response = controller.likePost(postID, null);

		// Then
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

		verifyNoInteractions(postService);
	}

}