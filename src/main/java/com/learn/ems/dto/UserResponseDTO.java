package com.learn.ems.dto;

import com.learn.ems.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Set;

@Builder
@Schema(description = "Data Transfer Object for sending user information in responses")
public record UserResponseDTO(

        @Schema(description = "Unique identifier of the user", example = "101")
        Long id,

        @Schema(description = "Full name of the user", example = "John Doe")
        String name,

        @Schema(description = "Email address of the user", example = "john.doe@example.com")
        String email,

        @Schema(description = "Account status: true if the user is enabled", example = "true")
        Boolean enabled,

        @Schema(description = "Set of roles assigned to the user")
        Set<Role> role
) {}
