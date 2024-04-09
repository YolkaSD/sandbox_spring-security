package com.example.service;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.mapper.UserMapper;
import com.example.model.RegistrationForm;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String processRegistration(RegistrationForm form, Errors errors) {
        if (errors.hasErrors()) {
            return "registration";
        }
        form.setPassword(passwordEncoder.encode(form.getPassword()));
        User user = userMapper.toUser(form);
        user.setAuthorities(Collections.singleton(roleRepository.findByAuthority("ROLE_USER")
                .orElseThrow(() -> new ObjectNotFoundException(Role.class, "Роли 'ROLE_USER' не существует"))));
        userRepo.save(user);
        return "redirect:/login";
    }

}
