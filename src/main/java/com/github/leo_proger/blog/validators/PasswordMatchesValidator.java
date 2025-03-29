package com.github.leo_proger.blog.validators;

import com.github.leo_proger.blog.annotations.PasswordMatches;
import com.github.leo_proger.blog.dto.UserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        UserDTO user = (UserDTO) value;

        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("passwordConfirmation")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
