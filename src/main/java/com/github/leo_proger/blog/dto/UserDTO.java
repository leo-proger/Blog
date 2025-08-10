package com.github.leo_proger.blog.dto;


import com.github.leo_proger.blog.annotation.PasswordMatches;
import com.github.leo_proger.blog.annotation.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@PasswordMatches
@EqualsAndHashCode
public class UserDTO {

	@ValidUsername
	@Size(min = 4, max = 30, message = "Username length must be from 3 to 30")
	private String username;

	@Size(min = 5, message = "Password length must be 5 or more")
	private String password;

	@NotBlank(message = "Password confirmation cannot be empty")
	private String passwordConfirmation;

}
