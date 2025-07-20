package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for registering a new user")
public record RegisterRequestDTO(

        @NotBlank
        @Size(min = 2, max = 50)
        @Schema(description = "Full name of the user", example = "Alice Johnson", required = true)
        String name,

        @NotBlank
        @Email
        @Schema(description = "User's email address", example = "alice@example.com", required = true)
        String email,

        @NotBlank
        @Size(min = 6, max = 100)
        @Schema(description = "Password for the account (minimum 6 characters)", example = "P@ssw0rd", required = true)
        String password

) {}

