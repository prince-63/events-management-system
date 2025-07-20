package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Error response for validation failures, extending ErrorResponseDTO")
public class ConstraintValidationErrorResponseDTO extends ErrorResponseDTO {

    @Schema(
            description = "A map containing field names as keys and corresponding validation error messages as values",
            example = "{\"email\": \"Email must be valid\", \"password\": \"Password must be at least 8 characters\"}"
    )
    private Map<String, String> validationErrors;

    public ConstraintValidationErrorResponseDTO(String apiPath, int errorCode, String errorMessage, LocalDateTime errorTime, Map<String, String> validationErrors) {
        super(apiPath, errorCode, errorMessage, errorTime);
        this.validationErrors = validationErrors;
    }
}

