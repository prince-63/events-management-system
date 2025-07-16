package com.learn.ems.controllers;

import com.learn.ems.dto.RegisterRequestDTO;
import com.learn.ems.dto.ResponseDTO;
import com.learn.ems.dto.UserResponseDTO;
import com.learn.ems.entity.User;
import com.learn.ems.mapper.UserMapper;
import com.learn.ems.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.learn.ems.constants.ResponseMessageConstants.*;
import static com.learn.ems.constants.UserApiEndPointsConstants.*;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(REGISTER_ATTENDEE)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerAttendee(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        User registeredAttendee = userService.registerAsAttendee(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(ATTENDEE_REGISTERED_SUCCESSFULLY.getMessage(), true, UserMapper.userResponseDTO(registeredAttendee)));
    }

    @PostMapping(REGISTER_ORGANIZER)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerOrganizer(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        User registeredOrganizer = userService.registerAsOrganizer(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(ORGANIZER_REGISTERED_SUCCESSFULLY.getMessage(), true, UserMapper.userResponseDTO(registeredOrganizer)));
    }

    @PostMapping(REGISTER_ADMIN)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerAdmin(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        User registeredAdmin = userService.registerAsAdmin(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(ADMIN_REGISTERED_SUCCESSFULLY.getMessage(), true, UserMapper.userResponseDTO(registeredAdmin)));
    }

}
