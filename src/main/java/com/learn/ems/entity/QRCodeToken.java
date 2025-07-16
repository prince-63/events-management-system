package com.learn.ems.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Represents the QR token issued for check-in")
@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class QRCodeToken {

    @Schema(description = "Unique ID of the QR token")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Secure token string", example = "f3a9d2c3-4bda-44f1-9e33-123456789abc")
    @NotBlank(message = "Token should not be blank")
    private String token;

    @Schema(description = "Expiration date/time for the QR token", example = "2025-08-01T10:00:00")
    @Future(message = "Expiration must be in the future")
    private LocalDateTime expiresAt;

    @Schema(description = "Indicates whether the token has been used", example = "false")
    private boolean used = false;

    @OneToOne(mappedBy = "qrCodeToken")
    private Registration registration;
}


