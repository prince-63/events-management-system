package com.learn.ems.services.impl;

import com.learn.ems.dto.EventRequestDTO;
import com.learn.ems.entity.Event;
import com.learn.ems.entity.User;
import com.learn.ems.exceptions.NotFoundException;
import com.learn.ems.mapper.EventMapper;
import com.learn.ems.repositories.EventRepository;
import com.learn.ems.services.EventService;
import com.learn.ems.services.PresignedUrlGeneratorService;
import com.learn.ems.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final PresignedUrlGeneratorService urlGeneratorService;

    @Override
    public Event createEvent(String organizerEmail, EventRequestDTO requestDTO) {
        User user = userService.findByEmail(organizerEmail);
        Event event = EventMapper.toModel(requestDTO);
        event.setOrganizer(user);
        return eventRepository.save(event);
    }

    @Override
    public Event uploadBannerImage(Long eventId, MultipartFile image) {
        Map<String, String> obj = urlGeneratorService.generate(image);
        String publicId = obj.get("public_id");
        String url = obj.get("url");
        Event dbEvent = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Event not found with %d id.", eventId)));
        dbEvent.setUrlId(publicId);
        dbEvent.setBannerUrl(url);
        return eventRepository.save(dbEvent);
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Event not found with %d id.", eventId)));
    }

    @Override
    public boolean existsById(Long eventId) {
        return eventRepository.existsById(eventId);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getEventsByOrganizer(Long organizerId) {
        User user = userService.getById(organizerId);
        return eventRepository.findByOrganizer(user);
    }

    @Override
    public Event updateEvent(Long eventId, EventRequestDTO requestDTO) {
        Event dbEvent = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Event not found with %d id.", eventId)));
        dbEvent.setTitle(requestDTO.title() != null ? requestDTO.title() : dbEvent.getTitle());
        dbEvent.setDescription(requestDTO.description() != null ? requestDTO.description() : dbEvent.getDescription());
        dbEvent.setStartTime(requestDTO.startTime() != null ? requestDTO.startTime() : dbEvent.getStartTime());
        dbEvent.setEndTime(requestDTO.endTime() != null ? requestDTO.endTime() : dbEvent.getEndTime());
        dbEvent.setLocation(requestDTO.location() != null ? requestDTO.location() : dbEvent.getLocation());
        dbEvent.setCapacity(requestDTO.capacity() != null ? requestDTO.capacity() : dbEvent.getCapacity());
        return eventRepository.save(dbEvent);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}
