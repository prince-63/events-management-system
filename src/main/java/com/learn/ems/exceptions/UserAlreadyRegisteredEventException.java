package com.learn.ems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
public class UserAlreadyRegisteredEventException extends RuntimeException {
    public UserAlreadyRegisteredEventException(String eventId) {
        super(String.format("User already registered for this event: %s", eventId));
    }
}
