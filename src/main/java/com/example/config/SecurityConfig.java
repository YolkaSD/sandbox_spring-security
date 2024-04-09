package com.example.config;

import com.example.config.util.JwtTokenUtils;
import com.example.domain.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User '" + username + "' not found"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserRepository userRepo) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService(userRepo));
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager,
                                           JwtTokenUtils jwtTokenUtils) throws Exception {
        return http.authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/").authenticated()
                                .anyRequest().permitAll())
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            String username = request.getParameter("username");
                            String password = request.getParameter("password");
                            authenticationManager.authenticate(
                                    new UsernamePasswordAuthenticationToken(username, password));
                            User user = (User) authentication.getPrincipal();
                            String token = jwtTokenUtils.generateToken(user);
                            response.addHeader("Authorization", "Bearer " + token);
                        }))
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/").permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
