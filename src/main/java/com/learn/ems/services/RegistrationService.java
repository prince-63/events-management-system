package com.learn.ems.services;

import com.learn.ems.entity.Registration;

import java.util.List;

/**
 * Service interface for managing registration of an events within the Event Management System.
 */
public interface RegistrationService {

    /**
     * Register an event
     * @param userId - id of the user
     * @param eventId - eventId of the event
     * @return - Registration object containing registration details
     */
    Registration registerUserForEvent(Long userId, Long eventId);

    /**
     * Cancel registration of an event
     * @param userId - id of the user
     * @param eventId - eventId of the event
     * @return - Registration object that cancel
     */
    Registration cancelRegistration(Long userId, Long eventId);

    /**
     * CheckIn event
     * @param userId - id of the user
     * @param eventId - eventId of the event
     * @return - Registration object
     */
    Registration checkInEvent(Long userId, Long eventId);

    /**
     * Get all Registrations by event id
     * @param eventId - id of the event
     * @return - list of registration object
     */
    List<Registration> getRegistrationsByEvent(Long eventId);

    /**
     * Get all Registration by used id
     * @param userId - id of the user
     * @return - List of registration object
     */
    List<Registration> getRegistrationsByUser(Long userId);

    /**
     * Get registration details
     * @param userId - id of the user
     * @param eventId - id of the event
     * @return - Registration object
     */
    Registration getRegistrationDetails(Long userId, Long eventId);

}
