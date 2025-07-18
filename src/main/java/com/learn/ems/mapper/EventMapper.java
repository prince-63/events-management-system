package com.learn.ems.mapper;

import com.learn.ems.dto.EventRequestDTO;
import com.learn.ems.dto.EventResponseDTO;
import com.learn.ems.entity.Event;

public class EventMapper {

    public static Event toModel(EventRequestDTO requestDTO) {
        return Event.builder()
                .title(requestDTO.title())
                .description(requestDTO.description())
                .startTime(requestDTO.startTime())
                .endTime(requestDTO.endTime())
                .capacity(requestDTO.capacity())
                .location(requestDTO.location())
                .build();
    }

    public static EventResponseDTO toDTO(Event event) {
        return EventResponseDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .capacity(event.getCapacity())
                .location(event.getLocation())
                .urlId(event.getUrlId())
                .bannerUrl(event.getBannerUrl())
                .build();
    }
}
