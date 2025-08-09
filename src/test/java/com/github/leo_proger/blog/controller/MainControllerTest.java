package com.github.leo_proger.blog.controller;


import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.repository.UserRepository;
import com.github.leo_proger.blog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class MainControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PostService postService;

	@InjectMocks
	private MainController controller;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders
				          .standaloneSetup(controller)
				          .setViewResolvers((viewName, locale) -> {
					          return (model, request, response) -> {};
				          })
				          .build();
	}

	@Test
	void index_UserNotAuthorized() throws Exception {
		mockMvc.perform(get("/"))
		       .andExpect(status().isOk())
		       .andExpect(view().name("main"))
		       .andExpect(model().attributeExists("posts"))
		       .andExpect(model().attributeDoesNotExist("likedPostsIDs"));

		verifyNoInteractions(userRepository);
	}

	@Test
	void index_UserAuthorized() throws Exception {
		// Given
		String username = "leo_proger";

		User user = new User();
		user.setUsername(username);

		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

		// When & Then
		mockMvc.perform(get("/").principal(() -> username))
		       .andExpect(status().isOk())
		       .andExpect(view().name("main"))
		       .andExpect(model().attributeExists("posts"))
		       .andExpect(model().attributeExists("likedPostsIDs"));
	}

}