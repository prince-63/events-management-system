package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "Request body for creating or updating an event")
public record EventRequestDTO(

        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        @Schema(description = "Title of the event", example = "Spring Boot Workshop", required = true)
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        @Schema(description = "Description of the event", example = "A full-day hands-on Spring Boot training session", required = true)
        String description,

        @NotNull(message = "Start time is required")
        @Schema(description = "Start time of the event", example = "2025-08-01T09:00:00", required = true)
        LocalDateTime startTime,

        @NotNull(message = "End time is required")
        @Schema(description = "End time of the event", example = "2025-08-01T17:00:00", required = true)
        LocalDateTime endTime,

        @NotBlank(message = "Location is required")
        @Size(max = 200, message = "Location cannot exceed 200 characters")
        @Schema(description = "Location where the event will be held", example = "Room 101, Tech Hub, Bangalore", required = true)
        String location,

        @NotNull(message = "Capacity is required")
        @Min(value = 1, message = "Capacity must be at least 1")
        @Schema(description = "Maximum number of attendees allowed", example = "50", required = true)
        Integer capacity
) {}

