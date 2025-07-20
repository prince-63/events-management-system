package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response body after successful logout")
public record LogoutResponseDTO(

        @Schema(description = "Email of the logged-out user", example = "user@example.com")
        String email

) {}
