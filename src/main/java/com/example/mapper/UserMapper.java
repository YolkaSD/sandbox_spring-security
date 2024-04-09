package com.example.mapper;
import com.example.domain.User;
import com.example.model.RegistrationForm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegistrationForm from);
}
