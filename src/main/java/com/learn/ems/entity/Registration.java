package com.learn.ems.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Represents a registration of a user for a specific event")
@Entity
@Table(name="registrations")
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Registration {

    @Schema(description = "Unique ID of the registration", example = "501")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Schema(description = "Indicates whether the user checked in", example = "false")
    private boolean checkedIn = false;

    @Schema(description = "Timestamp when the user registered", example = "2025-07-15T10:05:00")
    private LocalDateTime registeredAt = LocalDateTime.now();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "qr_token_id")
    private QRCodeToken qrCodeToken;

}


