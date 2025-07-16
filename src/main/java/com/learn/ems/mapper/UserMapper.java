package com.learn.ems.mapper;

import com.learn.ems.dto.RegisterRequestDTO;
import com.learn.ems.dto.UserResponseDTO;
import com.learn.ems.entity.User;

public class UserMapper {

    public static User toModel(RegisterRequestDTO requestDTO) {
        return User.builder()
                .name(requestDTO.name())
                .email(requestDTO.email())
                .password(requestDTO.password())
                .build();
    }

    public static UserResponseDTO userResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .role(user.getRole())
                .build();
    }
}
