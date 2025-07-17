package com.learn.ems.constants;

import lombok.Getter;

@Getter
public enum ErrorMessageConstants {
    EMAIL_ALREADY_EXISTS("A user with this email is already registered."),
    ATTENDEE_ALREADY_EXISTS("User is already registered as ATTENDEE."),
    ORGANIZER_ALREADY_EXISTS("User is already registered as ORGANIZER."),
    ATTENDEE_PASSWORD_MISMATCH("Please enter the same password that you used for attending account."),
    ORGANIZER_PASSWORD_MISMATCH("Please enter the same password that you used for organizer account."),
    ADMIN_ROLE_CONFLICT("User is already registered as ADMIN and cannot register as ATTENDEE or ORGANIZER."),
    ADMIN_ALREADY_EXISTS("A admin with this email is already registered."),
    PASSWORD_MISMATCH("Passwords don't match."),
    JWT_TOKEN_NOT_VALID("The JWT token is not valid."),
    EMAIL_NOT_EXISTS("A user with this %s email is not found."),

    INVALID_CREDENTIALS("Invalid email or password."),
    UNAUTHORIZED_ACCESS("You are not authorized to access this resource."),

    ENTITY_NOT_FOUND("Requested resource not found."),
    INTERNAL_SERVER_ERROR("An unexpected error occurred."),

    // ====== Constraint Validation Error Message ======
    CONSTRAINT_VALIDATION("Constraint Validation failed");

    private final String message;

    ErrorMessageConstants(String message) {
        this.message = message;
    }
}

