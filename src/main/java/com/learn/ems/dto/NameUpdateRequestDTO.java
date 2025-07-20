package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body for updating the user's name")
public record NameUpdateRequestDTO(

        @Schema(description = "New name of the user", example = "John Doe")
        String name

) {}

