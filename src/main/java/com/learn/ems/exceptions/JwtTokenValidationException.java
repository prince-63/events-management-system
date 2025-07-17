package com.learn.ems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JwtTokenValidationException extends RuntimeException{
    public JwtTokenValidationException(String message) {
        super(message);
    }
}
