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
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.learn.ems.constants.AuthenticationConstants.JWT_HEADER;
import static com.learn.ems.constants.ErrorMessageConstants.EMAIL_NOT_EXISTS;
import static com.learn.ems.constants.ResponseMessageConstants.*;
import static com.learn.ems.constants.UserApiEndPointsConstants.*;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(REGISTER_ATTENDEE)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerAttendee(
            @Valid @RequestBody RegisterRequestDTO requestDTO
    ) {
        User registeredAttendee = userService.registerAsAttendee(requestDTO);
        ResponseDTO<UserResponseDTO> response = buildResponse(ATTENDEE_REGISTERED_SUCCESSFULLY, registeredAttendee);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(REGISTER_ORGANIZER)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerOrganizer(
            @Valid @RequestBody RegisterRequestDTO requestDTO
    ) {
        User registeredOrganizer = userService.registerAsOrganizer(requestDTO);
        ResponseDTO<UserResponseDTO> response = buildResponse(ORGANIZER_REGISTERED_SUCCESSFULLY, registeredOrganizer);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(REGISTER_ADMIN)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerAdmin(
            @Valid @RequestBody RegisterRequestDTO requestDTO
    ) {
        User registeredAdmin = userService.registerAsAdmin(requestDTO);
        ResponseDTO<UserResponseDTO> response = buildResponse(ADMIN_REGISTERED_SUCCESSFULLY, registeredAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(FORM_LOGIN)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> formLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        ResponseDTO<UserResponseDTO> response = buildResponse(LOGIN_SUCCESSFUL, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(LOGIN)
    public ResponseEntity<ResponseDTO<LoginResponseDTO>> login(
            @RequestBody LoginRequestDTO requestDTO
    ) {
        LoginResponseDTO response = userService.login(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).header(JWT_HEADER, response.jwtToken()).body(new ResponseDTO<>(LOGIN_SUCCESSFUL, true, response));
    }

    @PostMapping(LOGOUT)
    public ResponseEntity<ResponseDTO<LogoutResponseDTO>> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.status(HttpStatus.OK).header(JWT_HEADER, "").body(new ResponseDTO<>(LOGOUT_SUCCESSFUL, true, new LogoutResponseDTO(email)));
    }

    @GetMapping(GET_ALL_USERS)
    public ResponseEntity<ResponseDTO<List<UserResponseDTO>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> usersDTO = users.stream().map(UserMapper::userResponseDTO).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(ALL_USERS_FETCHED, true, usersDTO));
    }

    @GetMapping(GET_CURRENT_USER)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> getCurrentUser() {
        User user = userService.findByEmail(getEmail());
        ResponseDTO<UserResponseDTO> response = buildResponse(USER_FETCH_SUCCESS, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(CHECK_USER_EXISTS_BY_EMAIL)
    public ResponseEntity<ResponseDTO<?>> checkUserExistsByEmail(
            @PathVariable String email
    ) {
        boolean isValid = userService.existsByEmail(email);
        if (isValid) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(USER_EXISTS_WITH_EMAIL, true, null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>(String.format(EMAIL_NOT_EXISTS, email), true, null));
        }
    }

    @PatchMapping(UPDATE_NAME)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> updateName(
            @RequestBody NameUpdateRequestDTO requestDTO
    ) {
        User user = userService.updateName(getEmail(), requestDTO.name());
        ResponseDTO<UserResponseDTO> response = buildResponse(USER_NAME_UPDATED_SUCCESSFULLY, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(UPDATE_PASSWORD)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> updatePassword(
            @RequestBody ChangePasswordRequestDTO requestDTO
    ) {
        User user = userService.updatePassword(getEmail(), requestDTO);
        ResponseDTO<UserResponseDTO> response = buildResponse(PASSWORD_UPDATED_SUCCESSFULLY, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(FORGOT_PASSWORD)
    public ResponseEntity<ResponseDTO<?>> forgotPassword(
            @PathVariable String email
    ) {
        userService.forgotPassword(email);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(PASSWORD_RESET_EMAIL_SENT, true, null));
    }

    @PostMapping(VERIFY_EMAIL_AND_CHANGE_PASSWORD)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> verifyEmailAndChangePassword(
            @RequestBody VerificationEmailAndPasswordChangeRequestDTO requestDTO
    ) {
        User user = userService.verifyEmailAndChangePassword(requestDTO);
        ResponseDTO<UserResponseDTO> response = buildResponse(PASSWORD_UPDATED_SUCCESSFULLY, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(DELETE_USER_BY_EMAIL)
    public ResponseEntity<ResponseDTO<?>> deleteUserByEmail(
            @PathVariable String email
    ) {
        userService.deleteByEmail(email);
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(USER_DELETED_SUCCESSFULLY, true, null));
    }

    @DeleteMapping(DELETE_USER_BY_ID)
    public ResponseEntity<ResponseDTO<?>> deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(USER_DELETED_SUCCESSFULLY, true, null));
    }

    private ResponseDTO<UserResponseDTO> buildResponse(String message, User user) {
        return new ResponseDTO<>(message, true, UserMapper.userResponseDTO(user));
    }

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
