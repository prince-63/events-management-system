package com.learn.ems.controllers;

import com.learn.ems.dto.EventRequestDTO;
import com.learn.ems.dto.EventResponseDTO;
import com.learn.ems.dto.ResponseDTO;
import com.learn.ems.entity.Event;
import com.learn.ems.mapper.EventMapper;
import com.learn.ems.services.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.learn.ems.constants.EventApiEndPointConstants.*;
import static com.learn.ems.constants.ResponseMessageConstants.*;

@RestController
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping(CREATE_EVENT)
    public ResponseEntity<ResponseDTO<EventResponseDTO>> createEvent(
            Authentication authentication, @RequestBody EventRequestDTO requestDTO
    ) {
        Event event = eventService.createEvent(authentication.getName(), requestDTO);
        ResponseDTO<EventResponseDTO> response = buildResponse(EVENT_CREATED_SUCCESSFULLY, event);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(UPLOAD_BANNER)
    public  ResponseEntity<ResponseDTO<EventResponseDTO>> uploadBannerImage(
            @PathVariable Long eventId, @RequestParam("imageFile") MultipartFile imageFile
    ) {
        Event event = eventService.uploadBannerImage(eventId, imageFile);
        ResponseDTO<EventResponseDTO> response = buildResponse(EVENT_BANNER_UPLOAD_SUCCESSFULLY, event);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(GET_EVENT_BY_ID)
    public ResponseEntity<ResponseDTO<EventResponseDTO>> getEventById(
            @PathVariable(value = "eventId") Long eventId
    ) {
        Event event = eventService.getEventById(eventId);
        ResponseDTO<EventResponseDTO> response = buildResponse(EVENT_FETCHED_SUCCESSFULLY, event);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(GET_ALL_EVENTS)
    public ResponseEntity<ResponseDTO<List<EventResponseDTO>>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        List<EventResponseDTO> response = events.stream().map(EventMapper::toDTO).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(EVENT_FETCHED_SUCCESSFULLY, true, response));
    }

    @GetMapping(GET_EVENTS_BY_ORGANIZER)
    public ResponseEntity<ResponseDTO<List<EventResponseDTO>>> getEventsByOrganizer(@PathVariable Long organizerId) {
        List<Event> events = eventService.getEventsByOrganizer(organizerId);
        List<EventResponseDTO> response = events.stream().map(EventMapper::toDTO).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(EVENT_FETCHED_SUCCESSFULLY, true, response));
    }

    @PatchMapping(UPDATE_EVENT)
    public ResponseEntity<ResponseDTO<EventResponseDTO>> updateEvent(@PathVariable Long eventId, @RequestBody EventRequestDTO dto) {
        final Event event = eventService.updateEvent(eventId, dto);
        ResponseDTO<EventResponseDTO> response = buildResponse(EVENT_UPDATED_SUCCESSFULLY, event);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(DELETE_EVENT)
    public ResponseEntity<ResponseDTO<?>> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(EVENT_UPDATED_SUCCESSFULLY, true, null));
    }

    private ResponseDTO<EventResponseDTO> buildResponse(String message, Event event) {
        return new ResponseDTO<>(message, true, EventMapper.toDTO(event));
    }
}
