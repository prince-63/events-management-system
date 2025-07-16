package com.learn.ems.exceptions;

import com.learn.ems.dto.ConstraintValidationErrorResponseDTO;
import com.learn.ems.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.learn.ems.constants.ErrorMessageConstants.CONSTRAINT_VALIDATION;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ConstraintValidationErrorResponseDTO> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request
    ) {
        Map<String, String> validationErrors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            validationErrors.put(field, message);
        });

        ConstraintValidationErrorResponseDTO errorResponse = new ConstraintValidationErrorResponseDTO(
                request.getRequestURI().replace("uri=", ""),
                HttpStatus.BAD_REQUEST.value(),
                CONSTRAINT_VALIDATION.getMessage(),
                LocalDateTime.now(),
                validationErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(
            Exception exception, WebRequest webRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setApiPath(webRequest.getDescription(false).replace("uri=", ""));
        errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setErrorMessage(exception.getMessage());
        errorResponse.setErrorTime(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler({
            UserAlreadyExistsException.class,
            InvalidPasswordException.class,
            UnauthorizedRoleChangeException.class,
            ResourceNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            RuntimeException exception, WebRequest webRequest
    ) {
        HttpStatus resolveStatus = resolveStatus(exception);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setApiPath(webRequest.getDescription(false).replace("uri=", ""));
        errorResponse.setErrorCode(resolveStatus.value());
        errorResponse.setErrorMessage(exception.getMessage());
        errorResponse.setErrorTime(LocalDateTime.now());
        return ResponseEntity.status(resolveStatus).body(errorResponse);
    }

    private HttpStatus resolveStatus(RuntimeException ex) {
        if (ex instanceof UserAlreadyExistsException) return HttpStatus.CONFLICT;
        if (ex instanceof InvalidPasswordException) return HttpStatus.UNAUTHORIZED;
        if (ex instanceof UnauthorizedRoleChangeException) return HttpStatus.FORBIDDEN;
        if (ex instanceof ResourceNotFoundException) return HttpStatus.NOT_FOUND;
        if (ex instanceof UserNotFoundException) return HttpStatus.NOT_FOUND;
        return HttpStatus.BAD_REQUEST;
    }

}