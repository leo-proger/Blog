package com.github.leo_proger.blog.controller;


import com.github.leo_proger.blog.dto.UserDTO;
import com.github.leo_proger.blog.exception.UserAlreadyExistsException;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@Test
	void signupGet_Success() throws Exception {
		mockMvc.perform(get("/users/signup"))
		       .andExpect(model().attributeExists("user"));

		verifyNoInteractions(userService);
	}

	@Test
	void signupPost_Success() throws Exception {
		// Given
		String username = "leo_proger";
		String password = "password";

		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(username);
		userDTO.setPassword(password);
		userDTO.setPasswordConfirmation(password);

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);

		Authentication auth = new UsernamePasswordAuthenticationToken(username, password);

		when(userService.save(userDTO)).thenReturn(user);
		when(userService.authenticate(eq(username), eq(password), anyCollection())).thenReturn(auth);

		// When & Then
		MvcResult result = mockMvc.perform(post("/users/signup")
				                                   .param("username", username)
				                                   .param("password", password)
				                                   .param("passwordConfirmation", password))
		                          .andExpect(status().is3xxRedirection())
		                          .andExpect(redirectedUrl("/"))
		                          .andExpect(request().sessionAttribute(SPRING_SECURITY_CONTEXT_KEY, notNullValue()))
		                          .andReturn();

		HttpSession session = result.getRequest().getSession(false);
		assertNotNull(session);

		SecurityContext sc = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
		assertNotNull(sc);
		assertEquals(username, sc.getAuthentication().getName());

		verify(userService, times(1)).save(any(UserDTO.class));
	}

	@Test
	void signupPost_PasswordDoesNotMatch_Error() throws Exception {
		// Given
		String username = "leo_proger";
		String password = "password";
		String passwordConfirmation = "a different password";

		// When & Then
		mockMvc.perform(post("/users/signup")
				                .param("username", username)
				                .param("password", password)
				                .param("passwordConfirmation", passwordConfirmation))
		       .andExpect(status().isOk())
		       .andExpect(view().name("signup"))
		       .andExpect(request().sessionAttribute(SPRING_SECURITY_CONTEXT_KEY, nullValue()));

		verifyNoInteractions(userService);
	}

	@Test
	void signupPost_UserAlreadyExists_Error() throws Exception {
		// Given
		String username = "leo_proger";
		String password = "password";

		when(userService.save(any(UserDTO.class))).thenThrow(UserAlreadyExistsException.class);

		// When & Then
		mockMvc.perform(post("/users/signup")
				                .flashAttr("userDTO", new UserDTO())
				                .param("username", username)
				                .param("password", password)
				                .param("passwordConfirmation", password))
		       .andExpect(status().isOk())
		       .andExpect(view().name("signup"))
		       .andExpect(request().sessionAttribute(SPRING_SECURITY_CONTEXT_KEY, nullValue()));

		verify(userService, never()).authenticate(anyString(), anyString(), anyCollection());
		verify(userService, times(1)).save(any(UserDTO.class));
	}

}