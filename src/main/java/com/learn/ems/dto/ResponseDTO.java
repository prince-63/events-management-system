package com.learn.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Generic response wrapper for all API responses")
public class ResponseDTO<T> {

    @Schema(description = "Response message giving brief details about the result", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Indicates if the operation was successful", example = "true")
    private Boolean success;

    @Schema(description = "Response data payload of generic type")
    private T data;
}
