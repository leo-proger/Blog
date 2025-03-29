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
    @NotNull(message = "Username cannot be empty")
    @NotEmpty(message = "Username cannot be empty")
    @ValidUsername
    @Size(min = 3, max = 30, message = "Username length must be of 3 to 30")
    private String username;

    @NotNull(message = "Password cannot be empty")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 5, message = "Password length must be of 5 and more")
    private String password;

    @NotNull(message = "Password confirmation cannot be empty")
    @NotEmpty(message = "Password confirmation cannot be empty")
    private String passwordConfirmation;
}
