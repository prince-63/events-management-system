package com.learn.ems.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventResponseDTO(Long id, String title, String description, LocalDateTime startTime, LocalDateTime endTime, String location, Integer capacity, String urlId, String bannerUrl) {

}
