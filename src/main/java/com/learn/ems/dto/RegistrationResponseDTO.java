package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "Response DTO containing registration details for an event")
@Builder
public record RegistrationResponseDTO(

        @Schema(description = "Unique ID of the registration", example = "101")
        Long id,

        @Schema(description = "ID of the event for which the registration was made", example = "55")
        Long eventId,

        @Schema(description = "ID of the user who registered", example = "23")
        Long userId,

        @Schema(description = "Indicates whether the user has checked in", example = "false")
        Boolean checkedIn,

        @Schema(description = "Timestamp when the user registered", example = "2025-07-15T10:30:00")
        LocalDateTime registeredAt,

        @Schema(description = "ID of the associated QR code token", example = "301")
        Long qrCodeTokenId

) {}
