package com.learn.ems.services.impl;

import com.learn.ems.entity.Event;
import com.learn.ems.entity.QRCodeToken;
import com.learn.ems.entity.Registration;
import com.learn.ems.entity.User;
import com.learn.ems.exceptions.BadRequestException;
import com.learn.ems.exceptions.ConflictException;
import com.learn.ems.exceptions.NotFoundException;
import com.learn.ems.repositories.QRCodeTokenRepository;
import com.learn.ems.repositories.RegistrationRepository;
import com.learn.ems.services.EventService;
import com.learn.ems.services.QRCodeTokenService;
import com.learn.ems.services.RegistrationService;
import com.learn.ems.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final QRCodeTokenService qrCodeTokenService;
    private final EventService eventService;
    private final UserService userService;
    private final QRCodeTokenRepository qrCodeTokenRepository;

    @Override
    public Registration registerUserForEvent(Long userId, Long eventId) {
        User user = userService.getById(userId);
        Event event = eventService.getEventById(eventId);

        registrationRepository.findByUserAndEvent(user, event).ifPresent(r -> {
            throw new ConflictException(event.getId().toString());
        });

        Registration registration = Registration.builder()
                .user(user)
                .event(event)
                .registeredAt(LocalDateTime.now())
                .checkedIn(false)
                .qrCodeToken(qrCodeTokenService.generateToken(user, event))
                .build();

        return registrationRepository.save(registration);
    }

    @Override
    public Registration cancelRegistration(Long userId, Long eventId) {
        boolean isUserExists = userService.existsById(userId);
        boolean isEventExists = eventService.existsById(eventId);

        if (isUserExists && isEventExists) {
            Registration registration = registrationRepository.findByUserIdAndEventId(userId, eventId)
                    .orElseThrow(() -> new EntityNotFoundException("Registration not found"));
            qrCodeTokenService.invalidateToken(registration.getQrCodeToken().getToken());
            registrationRepository.delete(registration);
            return registration;
        } else {
            throw new NotFoundException(String.format("User with %d userId and Event with %d eventId not found", userId, eventId));
        }
    }

    @Override
    public Registration checkInEvent(Long userId, Long eventId) {
        boolean isUserExists = userService.existsById(userId);
        boolean isEventExists = eventService.existsById(eventId);

        if (isUserExists && isEventExists) {
            Registration registration = registrationRepository.findByUserIdAndEventId(userId, eventId)
                    .orElseThrow(() -> new EntityNotFoundException("Registration not found"));

            if (registration.isCheckedIn() && registration.getQrCodeToken().isUsed()) {
                throw new ConflictException("User already checked in.");
            }

            QRCodeToken qrCodeToken = registration.getQrCodeToken();
            if (qrCodeToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                registration.setCheckedIn(true);
                qrCodeToken.setUsed(true);
                qrCodeTokenRepository.save(qrCodeToken);
                registrationRepository.save(registration);
                return registration;
            } else {
                throw new BadRequestException("Registration date expired");
            }
        } else {
            throw new NotFoundException(String.format("User with %d userId and Event with %d eventId not found", userId, eventId));
        }
    }

    @Override
    public List<Registration> getRegistrationsByEvent(Long eventId) {
        boolean isEventExists = eventService.existsById(eventId);
        if  (isEventExists) {
            return registrationRepository.findAllByEventId(eventId);
        } else {
            throw new NotFoundException(String.format("eventId is invalid: %d", eventId));
        }
    }

    @Override
    public List<Registration> getRegistrationsByUser(Long userId) {
        boolean isUserExists = userService.existsById(userId);
        if (isUserExists) {
            return registrationRepository.findAllByUserId(userId);
        } else {
            throw new NotFoundException(String.format("userId is invalid: %d", userId));
        }
    }

    @Override
    public Registration getRegistrationDetails(Long userId, Long eventId) {
        boolean isUserExists = userService.existsById(userId);
        boolean isEventExists = eventService.existsById(eventId);

        if (isUserExists && isEventExists) {
            return registrationRepository.findByUserIdAndEventId(userId, eventId).orElseThrow(() -> new EntityNotFoundException("Registration not found"));
        }
        else {
            throw new NotFoundException(String.format("User with %d userId and Event with %d eventId not found", userId, eventId));
        }
    }
}
