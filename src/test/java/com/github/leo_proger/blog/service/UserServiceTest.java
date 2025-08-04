package com.github.leo_proger.blog.service;


import com.github.leo_proger.blog.dto.UserDTO;
import com.github.leo_proger.blog.enums.UserRole;
import com.github.leo_proger.blog.exception.UserAlreadyExistsException;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private UserRepository userRepositoryMock;

	@InjectMocks
	private UserService userService;

	@Test
	void saveUser_Success() {
		// Given
		String username = "leo_proger";
		String password = "12345";
		String encodedPassword = "encoded12345";

		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(username);
		userDTO.setPassword(password);
		userDTO.setPasswordConfirmation(password);

		User expectedUser = new User();
		expectedUser.setUsername(username);
		expectedUser.setPassword(encodedPassword);
		expectedUser.setRole(UserRole.USER);

		// Mocks
		when(userRepositoryMock.findByUsername(anyString())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(userDTO.getPassword())).thenReturn(encodedPassword);
		when(userRepositoryMock.save(expectedUser)).thenReturn(expectedUser);

		// When
		User result = userService.save(userDTO);

		// Then
		assertEquals(expectedUser, result);

		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepositoryMock).save(userArgumentCaptor.capture());

		User savedUser = userArgumentCaptor.getValue();

		assertEquals(username, savedUser.getUsername());
		assertEquals(encodedPassword, savedUser.getPassword());
		assertTrue(savedUser.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.USER.getAuthority())));
		assertFalse(savedUser.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ADMIN.getAuthority())));

		verify(passwordEncoder).encode(any(String.class));
		verify(userRepositoryMock).save(any(User.class));
	}

	@Test
	void saveUser_UserAlreadyExists() {
		String username = "leo_proger";

		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(username);

		when(userRepositoryMock.findByUsername(anyString())).thenReturn(Optional.of(new User()));

		assertThrows(UserAlreadyExistsException.class, () -> userService.save(userDTO));

		verify(userRepositoryMock, never()).save(any(User.class));
	}

	@Test
	void authenticateUser_Success() {
		// Given
		String username = "leo_proger";
		String password = "12345";
		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(UserRole.USER.getAuthority()));

		// When
		userService.authenticate(username, password, authorities);

		// Then
		ArgumentCaptor<Authentication> authArgumentCaptor = ArgumentCaptor.forClass(Authentication.class);
		verify(authenticationManager).authenticate(authArgumentCaptor.capture());

		Authentication auth = authArgumentCaptor.getValue();

		assertEquals(username, auth.getPrincipal());
		assertEquals(password, auth.getCredentials());
		assertEquals(authorities, auth.getAuthorities());
	}

}