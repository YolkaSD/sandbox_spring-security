package com.example.model;

import com.example.valid.PasswordMatches;
import com.example.valid.UsernameExist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationForm {
    @NotNull(message = "Не должно быть пустым")
    @NotBlank(message = "Не должно быть пустым")
    @Size(min = 6, max = 20, message = "Длина 6-20")
    @Pattern(regexp = "^[\\w@.-]*$", message = "Только буквы, цифры и (@, ., -)")
    @Pattern(regexp = "^\\S*$", message = "Без пробелов")
    @UsernameExist
    private String username;
    @NotNull(message = "Не должно быть пустым")
    @NotBlank(message = "Не должно быть пустым")
    @Size(min = 8, max = 16, message = "Длина 6-16")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Должен содержать комбинацию букв верхнего и нижнего регистра, цифр и специальных символов")
    private String password;
    @PasswordMatches
    private String confirmPassword;
    private String email;
}
