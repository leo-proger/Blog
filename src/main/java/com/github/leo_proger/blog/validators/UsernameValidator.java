package com.github.leo_proger.blog.validators;

import com.github.leo_proger.blog.annotations.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    private static final String PATTERN = "^[\\wА-Яа-яЁё.-]+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(PATTERN);
    }
}
