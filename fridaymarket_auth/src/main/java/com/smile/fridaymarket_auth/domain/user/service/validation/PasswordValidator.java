package com.smile.fridaymarket_auth.domain.user.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9!@#$%^&*]{10,20}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.length() < 10 || password.length() > 20) {
            return false;
        }

        int count = 0;
        if (password.matches(".*[a-zA-Z].*")) count++;
        if (password.matches(".*[0-9].*")) count++;
        if (password.matches(".*[!@#$%^&*].*")) count++;

        return count >= 2;
    }
}

