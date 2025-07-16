package com.learn.ems.dto;

import com.learn.ems.entity.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserResponseDTO(Long id, String name, String email, Boolean enabled, Set<Role> role) {
}
