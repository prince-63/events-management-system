package com.learn.ems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyCheckedInException extends RuntimeException {
    public UserAlreadyCheckedInException(Long userId, Long eventId) {
        super(String.format("User already checked in with %d userId and %d eventId.", userId, eventId));
    }
}
