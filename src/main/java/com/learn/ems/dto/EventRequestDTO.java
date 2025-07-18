package com.learn.ems.dto;

import java.time.LocalDateTime;

public record EventRequestDTO(String title, String description, LocalDateTime startTime, LocalDateTime endTime, String location, Integer capacity) {
}
