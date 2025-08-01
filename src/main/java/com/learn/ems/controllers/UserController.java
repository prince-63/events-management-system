package com.learn.ems.controllers;

import com.learn.ems.dto.*;
import com.learn.ems.entity.User;
import com.learn.ems.mapper.UserMapper;
import com.learn.ems.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "User APIs", description = "Handles user registration, login, profile updates, and account operations")
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register Attendee", description = "Register a new user with attendee role")
    @PostMapping(REGISTER_ATTENDEE)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerAttendee(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        User registeredAttendee = userService.registerAsAttendee(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(buildResponse(ATTENDEE_REGISTERED_SUCCESSFULLY, registeredAttendee));
    }

    @Operation(summary = "Register Organizer", description = "Register a new user with organizer role")
    @PostMapping(REGISTER_ORGANIZER)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerOrganizer(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        User registeredOrganizer = userService.registerAsOrganizer(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(buildResponse(ORGANIZER_REGISTERED_SUCCESSFULLY, registeredOrganizer));
    }

    @Operation(summary = "Register Admin", description = "Register a new user with admin role")
    @PostMapping(REGISTER_ADMIN)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerAdmin(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        User registeredAdmin = userService.registerAsAdmin(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(buildResponse(ADMIN_REGISTERED_SUCCESSFULLY, registeredAdmin));
    }

    @Operation(summary = "Form Login", description = "Handle form login and return authenticated user info")
    @PostMapping(FORM_LOGIN)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> formLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(buildResponse(LOGIN_SUCCESSFUL, userService.findByEmail(auth.getName())));
    }

    @Operation(summary = "JWT Login", description = "Authenticate using credentials and return JWT token")
    @PostMapping(LOGIN)
    public ResponseEntity<ResponseDTO<LoginResponseDTO>> login(@RequestBody LoginRequestDTO requestDTO) {
        LoginResponseDTO response = userService.login(requestDTO);
        return ResponseEntity.ok().header(JWT_HEADER, response.jwtToken()).body(new ResponseDTO<>(LOGIN_SUCCESSFUL, true, response));
    }

    @Operation(summary = "Logout", description = "Logs the user out and clears authentication")
    @PostMapping(LOGOUT)
    public ResponseEntity<ResponseDTO<LogoutResponseDTO>> logout() {
        String email = getEmail();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().header(JWT_HEADER, "").body(new ResponseDTO<>(LOGOUT_SUCCESSFUL, true, new LogoutResponseDTO(email)));
    }

    @Operation(summary = "Get All Users", description = "Returns a list of all registered users")
    @GetMapping(GET_ALL_USERS)
    public ResponseEntity<ResponseDTO<List<UserResponseDTO>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ResponseDTO<>(ALL_USERS_FETCHED, true, users.stream().map(UserMapper::userResponseDTO).toList()));
    }

    @Operation(summary = "Get Current User", description = "Fetches the currently authenticated user")
    @GetMapping(GET_CURRENT_USER)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> getCurrentUser() {
        return ResponseEntity.ok(buildResponse(USER_FETCH_SUCCESS, userService.findByEmail(getEmail())));
    }

    @Operation(summary = "Check User Existence by Email", description = "Verifies if a user exists with given email")
    @GetMapping(CHECK_USER_EXISTS_BY_EMAIL)
    public ResponseEntity<ResponseDTO<?>> checkUserExistsByEmail(@PathVariable String email) {
        if (userService.existsByEmail(email)) {
            return ResponseEntity.ok(new ResponseDTO<>(USER_EXISTS_WITH_EMAIL, true, null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>(String.format(EMAIL_NOT_EXISTS, email), true, null));
    }

    @Operation(summary = "Update Name", description = "Update the name of the currently authenticated user")
    @PatchMapping(UPDATE_NAME)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> updateName(@RequestBody NameUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(buildResponse(USER_NAME_UPDATED_SUCCESSFULLY, userService.updateName(getEmail(), requestDTO.name())));
    }

    @Operation(summary = "Update Password", description = "Change password of the currently authenticated user")
    @PatchMapping(UPDATE_PASSWORD)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> updatePassword(@RequestBody ChangePasswordRequestDTO requestDTO) {
        return ResponseEntity.ok(buildResponse(PASSWORD_UPDATED_SUCCESSFULLY, userService.updatePassword(getEmail(), requestDTO)));
    }

    @Operation(summary = "Forgot Password", description = "Send verification code to email for password reset")
    @PostMapping(FORGOT_PASSWORD)
    public ResponseEntity<ResponseDTO<?>> forgotPassword(@PathVariable String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok(new ResponseDTO<>(PASSWORD_RESET_EMAIL_SENT, true, null));
    }

    @Operation(summary = "Verify Email & Change Password", description = "Verify email and verification code, and change password")
    @PostMapping(VERIFY_EMAIL_AND_CHANGE_PASSWORD)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> verifyEmailAndChangePassword(@RequestBody VerificationEmailAndPasswordChangeRequestDTO requestDTO) {
        return ResponseEntity.ok(buildResponse(PASSWORD_UPDATED_SUCCESSFULLY, userService.verifyEmailAndChangePassword(requestDTO)));
    }

    @Operation(summary = "Delete User by Email", description = "Delete a user by their email")
    @DeleteMapping(DELETE_USER_BY_EMAIL)
    public ResponseEntity<ResponseDTO<?>> deleteUserByEmail(@PathVariable String email) {
        userService.deleteByEmail(email);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ResponseDTO<>(USER_DELETED_SUCCESSFULLY, true, null));
    }

    @Operation(summary = "Delete User by ID", description = "Delete a user by their unique ID")
    @DeleteMapping(DELETE_USER_BY_ID)
    public ResponseEntity<ResponseDTO<?>> deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ResponseDTO<>(USER_DELETED_SUCCESSFULLY, true, null));
    }

    private ResponseDTO<UserResponseDTO> buildResponse(String message, User user) {
        return new ResponseDTO<>(message, true, UserMapper.userResponseDTO(user));
    }

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
