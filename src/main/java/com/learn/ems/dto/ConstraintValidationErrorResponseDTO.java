package com.learn.ems.dto;

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
public class ConstraintValidationErrorResponseDTO extends ErrorResponseDTO {
    private Map<String, String> validationErrors;

    public ConstraintValidationErrorResponseDTO(String apiPath, int errorCode, String errorMessage, LocalDateTime errorTime, Map<String, String> validationErrors) {
        super(apiPath, errorCode, errorMessage, errorTime);
        this.validationErrors = validationErrors;
    }
}
