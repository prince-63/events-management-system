package com.learn.ems.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Represents an event created by an organizer")
@Entity
@Table(name="events")
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Event extends BaseEntity {

    @Schema(description = "Unique ID of the event", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Title of the event", example = "Spring Boot Workshop", required = true)
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Description of the event", example = "Hands-on Spring Boot and REST API workshop")
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(description = "Start time of the event", example = "2025-08-01T10:00:00")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @Schema(description = "End time of the event", example = "2025-08-01T13:00:00")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @Schema(description = "Location of the event", example = "Mumbai Convention Center")
    @NotBlank(message = "Location is required")
    private String location;

    @Schema(description = "Maximum number of seats", example = "100")
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @Schema(description = "URL of the event banner image", example = "https://cdn.example.com/images/banner.jpg")
    private String bannerUrl;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false)
    private User organizer;

    @JsonManagedReference
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Registration> registrations;
}


