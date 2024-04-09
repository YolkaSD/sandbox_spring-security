package com.example.valid;

import com.example.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernameExistValidator implements ConstraintValidator<UsernameExist, String> {

    private final UserRepository userRepo;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return userRepo.findByUsername(username).isEmpty();
    }
}
