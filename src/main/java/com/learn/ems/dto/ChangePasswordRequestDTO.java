package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for changing user password")
public record ChangePasswordRequestDTO(

        @NotBlank(message = "Current password must not be blank")
        @Size(min = 6, max = 100, message = "Current password must be between 6 and 100 characters")
        @Schema(description = "Current password of the user", example = "oldPass123", required = true)
        String currentPassword,

        @NotBlank(message = "New password must not be blank")
        @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
        @Schema(description = "New password to be set", example = "newSecurePass456", required = true)
        String newPassword

) {}
