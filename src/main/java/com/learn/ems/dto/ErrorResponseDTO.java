package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Schema(description = "Generic structure for API error responses")
public class ErrorResponseDTO {

    @Schema(
            description = "The endpoint path where the error occurred",
            example = "/api/events/123"
    )
    private String apiPath;

    @Schema(
            description = "HTTP status code associated with the error",
            example = "404"
    )
    private int errorCode;

    @Schema(
            description = "Detailed error message",
            example = "Event not found with the given ID"
    )
    private String errorMessage;

    @Schema(
            description = "The time when the error occurred",
            example = "2025-07-15T14:30:00"
    )
    private LocalDateTime errorTime;
}
