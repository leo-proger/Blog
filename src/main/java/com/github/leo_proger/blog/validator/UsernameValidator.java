package com.github.leo_proger.blog.validator;

import com.github.leo_proger.blog.annotation.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    private static final String PATTERN = "^[a-zA-Zа-яА-ЯЁё._\\-0-9]+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(PATTERN);
    }
}
