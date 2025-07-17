package com.learn.ems.constants;

import lombok.Getter;

@Getter
public enum ResponseMessageConstants {

    LOGIN_SUCCESSFUL("Login successful."),
    LOGOUT_SUCCESSFUL("Logout successful."),

    ATTENDEE_REGISTERED_SUCCESSFULLY("User registered successfully."),
    ORGANIZER_REGISTERED_SUCCESSFULLY("Organizer registered successfully."),
    ADMIN_REGISTERED_SUCCESSFULLY("Admin registered successfully."),
    USER_FETCH_SUCCESS("User details fetched successfully."),
    ALL_USERS_FETCHED("All users fetched successfully."),
    USER_EXISTS_WITH_EMAIL("User exists with email address."),
    USER_NAME_UPDATED_SUCCESSFULLY("User name updated successfully."),
    PASSWORD_UPDATED_SUCCESSFULLY("Password updated successfully."),
    PASSWORD_RESET_EMAIL_SENT("Password reset email sent successfully."),
    PASSWORD_RESET_SUCCESSFUL("Password reset successful."),
    USER_DELETED_SUCCESSFULLY("User deleted successfully."),

    EMAIL_VERIFIED_SUCCESSFULLY("Email verified successfully."),
    TOKEN_VERIFICATION_SUCCESS("Verification token validated successfully."),

    EVENT_CREATED_SUCCESSFULLY("Event created successfully."),
    EVENT_UPDATED_SUCCESSFULLY("Event updated successfully."),
    EVENT_DELETED_SUCCESSFULLY("Event deleted successfully."),
    EVENT_FETCHED_SUCCESSFULLY("Event details fetched successfully."),
    ALL_EVENTS_FETCHED("All events fetched successfully."),
    UPCOMING_EVENTS_FETCHED("Upcoming events fetched successfully."),
    PAST_EVENTS_FETCHED("Past events fetched successfully."),
    EVENT_REGISTRATION_SUCCESSFUL("User registered for the event successfully."),
    EVENT_REGISTRATION_CANCELLED("Event registration cancelled successfully."),
    REGISTRATION_ALREADY_EXISTS("User already registered for this event."),
    EVENT_REGISTRATION_FETCHED("Registration details fetched successfully."),
    CHECKIN_SUCCESSFUL("Check-in successful via QR code."),

    QR_CODE_EXPIRED("QR code has expired."),
    QR_CODE_ALREADY_USED("QR code has already been used."),
    QR_CODE_GENERATED("QR code generated successfully."),

    ACTION_COMPLETED("Action completed successfully."),
    REQUEST_PROCESSED_SUCCESSFULLY("Request processed successfully."),
    DATA_FETCHED_SUCCESSFULLY("Data fetched successfully."),
    NO_RECORD_FOUND("No records found."),
    UNAUTHORIZED_ACCESS("You are not authorized to perform this action."),
    INTERNAL_SERVER_ERROR("An internal server error occurred.");

    private final String message;

    ResponseMessageConstants(String message) {
        this.message = message;
    }
}

