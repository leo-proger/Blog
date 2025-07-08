package com.github.leo_proger.blog.services;

import com.github.leo_proger.blog.dtos.UserDTO;
import com.github.leo_proger.blog.enums.UserRole;
import com.github.leo_proger.blog.exceptions.UserAlreadyExistsException;
import com.github.leo_proger.blog.models.User;
import com.github.leo_proger.blog.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User \"" + userDTO.getUsername() + "\" is already exists");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(UserRole.USER);

        return userRepository.save(user);
    }

    public void authenticateUser(User user) {
        // Authenticate a user
        Authentication authResponse = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword(), user.getAuthorities()
        );
        // Save it in security context
        SecurityContextHolder.getContext().setAuthentication(authResponse);
    }
}
