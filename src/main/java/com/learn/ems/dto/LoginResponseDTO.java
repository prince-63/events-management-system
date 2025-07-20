package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing JWT token after successful login")
public record LoginResponseDTO(

        @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR...")
        String jwtToken

) {}
