package com.learn.ems.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User role enumeration")
public enum Role {
    ADMIN,
    ORGANIZER,
    ATTENDEE
}

