package com.example.service;

import com.example.model.RegistrationForm;
import org.springframework.validation.Errors;

public interface UserService {
    String processRegistration(RegistrationForm form, Errors errors);
}