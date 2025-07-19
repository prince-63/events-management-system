package com.learn.ems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QRCodeTokenNotFoundException extends RuntimeException {
    public QRCodeTokenNotFoundException(String token) {
        super(String.format("Token not found: %s", token));
    }
}
