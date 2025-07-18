package com.learn.ems.services;

import com.learn.ems.dto.EventRequestDTO;
import com.learn.ems.entity.Event;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for managing events within the Event Management System.
 */
public interface EventService {

    /**
     * Creates a new event with the provided details.
     *
     * @param requestDTO the event request data including title, description, start time, etc.
     * @return the created Event object
     */
    Event createEvent(String organizerEmail, EventRequestDTO requestDTO);

    /**
     * Upload a banner image
     * @param eventId - it is used to find the current event to attach the banner
     * @param image - image file
     * @return the updated event will be retrieved.
     */
    Event uploadBannerImage(Long eventId, MultipartFile image);

    /**
     * Retrieves a specific event by its unique ID.
     *
     * @param eventId the unique identifier of the event
     * @return the Event object if found
     */
    Event getEventById(Long eventId);

    /**
     * Retrieves all events available in the system.
     *
     * @return list of all Event objects
     */
    List<Event> getAllEvents();

    /**
     * Retrieves all events organized by a specific user.
     *
     * @param organizerId the unique email of the organizer (User)
     * @return list of Event objects created by the organizer
     */
    List<Event> getEventsByOrganizer(Long organizerId);

    /**
     * Updates the details of an existing event.
     *
     * @param eventId the unique ID of the event to be updated
     * @param dto the updated event data
     * @return the updated Event object
     */
    Event updateEvent(Long eventId, EventRequestDTO dto);

    /**
     * Deletes a specific event by its unique ID.
     *
     * @param eventId the ID of the event to delete
     */
    void deleteEvent(Long eventId);
}

