package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for verifying email and changing the password using a verification code")
public record VerificationEmailAndPasswordChangeRequestDTO(

        @NotBlank
        @Email
        @Schema(description = "Email address associated with the account", example = "user@example.com", required = true)
        String email,

        @NotBlank
        @Size(min = 6, max = 100)
        @Schema(description = "New password to set", example = "NewP@ssword123", required = true)
        String newPassword,

        @NotBlank
        @Schema(description = "Verification code sent to the email", example = "982134", required = true)
        String verificationCode

) {}

