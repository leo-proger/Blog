package com.github.leo_proger.blog.service;


import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CustomUserDetailsService userDetailsService;

	@Test
	void loadUserByUsername() {
		// Given
		String username = "leo_proger";

		User user = new User();
		user.setUsername(username);

		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

		// When
		userDetailsService.loadUserByUsername(username);

		// Then
		verify(userRepository).findByUsername(anyString());
	}

}