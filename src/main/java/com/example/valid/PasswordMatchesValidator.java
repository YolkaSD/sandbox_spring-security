package com.example.valid;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, String> {

    private final HttpServletRequest request;

    @Override
    public boolean isValid(String confirmPassword, ConstraintValidatorContext context) {
        String password = request.getParameter("password");
        return password != null && password.equals(confirmPassword);
    }
}
