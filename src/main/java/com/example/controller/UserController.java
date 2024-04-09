package com.example.controller;

import com.example.model.RegistrationForm;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@CrossOrigin("http://localhost:8080")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/registration")
    public String showRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String processRegistration(
            @ModelAttribute("registrationForm") @Valid RegistrationForm form, Errors errors) {
        return userService.processRegistration(form, errors);
    }

    @ModelAttribute("registrationForm")
    public RegistrationForm registrationForm() {
        return new RegistrationForm();
    }
}
