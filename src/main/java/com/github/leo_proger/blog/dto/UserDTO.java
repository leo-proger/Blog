package com.github.leo_proger.blog.dto;

import com.github.leo_proger.blog.annotations.ValidUsername;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    @ValidUsername
    @Size(min = 3, max = 30, message = "Username length must be from 3 to 30")
    private String username;

    @Size(min = 5, message = "Password length must be 3 or more")
    private String password;

    @NotNull(message = "Password confirmation cannot be empty")
    @NotEmpty(message = "Password confirmation cannot be empty")
    private String passwordConfirmation;
}
