package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Response DTO representing detailed validation errors for invalid method arguments"
)
public class MethodArgumentValidationResponseDTO extends ConstraintValidationErrorResponseDTO {

}
