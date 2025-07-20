package com.learn.ems.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegistrationResponseDTO(
        Long id,
        Long eventId,
        Long userId,
        Boolean checkedIn,
        LocalDateTime registeredAt,
        Long qrCodeTokenId
) {
}
