package com.learn.ems.exceptions;

import com.learn.ems.dto.ErrorResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(
            Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(ErrorResponseDTO.builder()
                .apiPath(webRequest.getDescription(false))
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now()).build(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({
            UserAlreadyExistsException.class,
            InvalidPasswordException.class,
            UnauthorizedRoleChangeException.class,
            ResourceNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            RuntimeException exception, WebRequest webRequest
    ) {
        HttpStatus resolveStatus = resolveStatus(exception);
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .apiPath(webRequest.getDescription(false))
                .errorCode(resolveStatus)
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        return ResponseEntity.status(resolveStatus).body(errorResponseDTO);
    }

    private HttpStatus resolveStatus(RuntimeException ex) {
        if (ex instanceof UserAlreadyExistsException) return HttpStatus.CONFLICT;
        if (ex instanceof InvalidPasswordException) return HttpStatus.UNAUTHORIZED;
        if (ex instanceof UnauthorizedRoleChangeException) return HttpStatus.FORBIDDEN;
        if (ex instanceof ResourceNotFoundException) return HttpStatus.NOT_FOUND;
        return HttpStatus.BAD_REQUEST;
    }

}
