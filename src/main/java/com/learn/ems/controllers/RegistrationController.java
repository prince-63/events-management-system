package com.learn.ems.controllers;

import com.learn.ems.dto.RegistrationResponseDTO;
import com.learn.ems.dto.ResponseDTO;
import com.learn.ems.entity.Registration;
import com.learn.ems.mapper.RegistrationMapper;
import com.learn.ems.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.learn.ems.constants.RegistrationApiEndPointsConstants.*;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping(REGISTER_EVENT)
    public ResponseEntity<ResponseDTO<RegistrationResponseDTO>> register(@PathVariable Long eventId, @PathVariable Long userId) {
        Registration registration = registrationService.registerUserForEvent(userId, eventId);
        var response = buildResponse("Event Registered Successfully", registration);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(CANCEL_REGISTER_EVENT)
    public ResponseEntity<ResponseDTO<RegistrationResponseDTO>> cancelRegistration(@PathVariable Long eventId, @PathVariable Long userId) {
        Registration registration = registrationService.cancelRegistration(userId, eventId);
        var response = buildResponse("Event Cancelled Successfully", registration);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(CHECK_IN_USER)
    public ResponseEntity<ResponseDTO<RegistrationResponseDTO>> checkInEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        Registration registration = registrationService.checkInEvent(userId, eventId);
        var response = buildResponse("Event Checked Successfully", registration);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(GET_REGISTERED_EVENTS)
    public ResponseEntity<ResponseDTO<List<RegistrationResponseDTO>>> getRegistrationsByEvent(@PathVariable Long eventId) {
        List<Registration> registrations = registrationService.getRegistrationsByEvent(eventId);
        var response = buildResponseList(registrations);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(GET_REGISTERED_EVENT_BY_USER)
    public ResponseEntity<ResponseDTO<List<RegistrationResponseDTO>>> getRegistrationsByUser(@PathVariable Long userId) {
        List<Registration> registrations = registrationService.getRegistrationsByUser(userId);
        var response = buildResponseList(registrations);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(GET_REGISTERED_EVENT_DETAILS)
    public ResponseEntity<ResponseDTO<RegistrationResponseDTO>> getRegistrationDetails(@PathVariable Long userId, @PathVariable Long eventId) {
        Registration registration = registrationService.getRegistrationDetails(userId, eventId);
        var response = buildResponse("Registration Details Found", registration);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private ResponseDTO<List<RegistrationResponseDTO>> buildResponseList(List<Registration> registrations) {
        return new ResponseDTO<>("Registrations Found", true, registrations.stream().map(RegistrationMapper::toDTO).toList());
    }

    private ResponseDTO<RegistrationResponseDTO> buildResponse(String message, Registration registration) {
        return new ResponseDTO<>(message, true, RegistrationMapper.toDTO(registration));
    }
}
