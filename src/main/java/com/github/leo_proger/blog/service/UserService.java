package com.github.leo_proger.blog.service;

import com.github.leo_proger.blog.dto.UserDTO;
import com.github.leo_proger.blog.enums.UserRole;
import com.github.leo_proger.blog.exception.UserAlreadyExistsException;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User save(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User \"" + userDTO.getUsername() + "\" already exists");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(UserRole.USER);

        return userRepository.save(user);
    }

    public Authentication authenticate(String username, String rawPassword, Collection<? extends GrantedAuthority> authorities) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                username,
                rawPassword,
                authorities
        );
        return authenticationManager.authenticate(authToken);
    }
}
