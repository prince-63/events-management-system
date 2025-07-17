package com.learn.ems.controllers;

import com.learn.ems.dto.*;
import com.learn.ems.entity.User;
import com.learn.ems.mapper.UserMapper;
import com.learn.ems.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.learn.ems.constants.AuthenticationConstants.JWT_HEADER;
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

    @PostMapping(FORM_LOGIN)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> formLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(LOGIN_SUCCESSFUL.getMessage(), true, UserMapper.userResponseDTO(user)));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<ResponseDTO<LoginResponseDTO>> login(@RequestBody LoginRequestDTO requestDTO) {
        LoginResponseDTO response = userService.login(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).header(JWT_HEADER, response.jwtToken()).body(new ResponseDTO<>(LOGIN_SUCCESSFUL.getMessage(), true, response));
    }

    @PostMapping(LOGOUT)
    public ResponseEntity<ResponseDTO<LogoutResponseDTO>> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.status(HttpStatus.OK).header(JWT_HEADER, "").body(new ResponseDTO<>(LOGOUT_SUCCESSFUL.getMessage(), true, new LogoutResponseDTO(email)));
    }

}
