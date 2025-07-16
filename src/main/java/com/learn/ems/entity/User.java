package com.learn.ems.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Schema(description = "Represents a user in the system (Admin, Organizer, Attendee)")
@Entity
@Table(name="users")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Schema(description = "Unique identifier of the user", example = "101")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Unique email address of the user", example = "john@example.com", required = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @Schema(description = "Password for authentication", example = "secret123", required = true)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(description = "Indicates if the user is enabled", example = "true")
    private boolean enabled = true;

    @Schema(description = "Role assigned to the user", example = "ORGANIZER")
    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonManagedReference
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Event> events;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Registration> registrations;
}
