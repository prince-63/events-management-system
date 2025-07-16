package com.learn.ems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // 403
public class UnauthorizedRoleChangeException extends RuntimeException {
    public UnauthorizedRoleChangeException(String message) {
        super(message);
    }
}