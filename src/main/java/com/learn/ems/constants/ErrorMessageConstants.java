package com.learn.ems.constants;

public final class ErrorMessageConstants {
    public static final String EMAIL_ALREADY_EXISTS = "A user with this email is already registered.";
    public static final String ATTENDEE_ALREADY_EXISTS = "User is already registered as ATTENDEE.";
    public static final String ORGANIZER_ALREADY_EXISTS = "User is already registered as ORGANIZER.";
    public static final String ATTENDEE_PASSWORD_MISMATCH = "Please enter the same password that you used for attending account.";
    public static final String ORGANIZER_PASSWORD_MISMATCH = "Please enter the same password that you used for organizer account.";
    public static final String ADMIN_ROLE_CONFLICT = "User is already registered as ADMIN and cannot register as ATTENDEE or ORGANIZER.";
    public static final String ADMIN_ALREADY_EXISTS = "A admin with this email is already registered.";
    public static final String PASSWORD_MISMATCH = "Passwords don't match.";
    public static final String CHANGE_PASSWORD_MISMATCH = "Your previous passwords don't match.";
    public static final String JWT_TOKEN_NOT_VALID = "The JWT token is not valid.";
    public static final String EMAIL_NOT_EXISTS = "A user with this %s email is not found.";
    public static final String UNAUTHORIZED_ACCESS = "You are not authorized to access this resource.";
    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred.";
    public static final String CONSTRAINT_VALIDATION = "Constraint Validation failed";
    public static final String USER_WITH_ID_NOT_EXISTS = "A user with this %s id is not found.";
}
