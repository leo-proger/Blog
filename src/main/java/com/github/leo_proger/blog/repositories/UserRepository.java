package com.github.leo_proger.blog.repositories;

import com.github.leo_proger.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByUsername(String login);
}
