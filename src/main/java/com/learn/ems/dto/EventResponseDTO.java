package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Data Transfer Object representing an Event response")
public record EventResponseDTO(

        @Schema(description = "Unique identifier of the event", example = "1")
        Long id,

        @Schema(description = "Title of the event", example = "Tech Conference 2025")
        String title,

        @Schema(description = "Detailed description of the event", example = "Annual tech meetup for developers and startups.")
        String description,

        @Schema(description = "Event start time", example = "2025-08-01T10:00:00")
        LocalDateTime startTime,

        @Schema(description = "Event end time", example = "2025-08-01T16:00:00")
        LocalDateTime endTime,

        @Schema(description = "Location where the event is held", example = "Bangalore International Exhibition Centre")
        String location,

        @Schema(description = "Maximum number of attendees", example = "500")
        Integer capacity,

        @Schema(description = "Public-friendly URL ID for sharing", example = "tech-conf-2025")
        String urlId,

        @Schema(description = "Banner image URL for the event", example = "https://res.cloudinary.com/demo/image/upload/v1699000000/event-banner.png")
        String bannerUrl

) {}
