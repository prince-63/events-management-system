package com.learn.ems.controllers;

import com.learn.ems.dto.*;
import com.learn.ems.entity.User;
import com.learn.ems.mapper.UserMapper;
import com.learn.ems.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Tag(name = "User APIs", description = "Endpoints for user registration, login, profile management, and password operations")
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register an attendee", description = "Registers a new user as an attendee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendee registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "409", description = "Email already registered", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PostMapping(REGISTER_ATTENDEE)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerAttendee(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        User registeredAttendee = userService.registerAsAttendee(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(ATTENDEE_REGISTERED_SUCCESSFULLY.getMessage(), true, UserMapper.userResponseDTO(registeredAttendee)));
    }

    @Operation(summary = "Register a new organizer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organizer registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping(REGISTER_ORGANIZER)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerOrganizer(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        User registeredOrganizer = userService.registerAsOrganizer(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(ORGANIZER_REGISTERED_SUCCESSFULLY.getMessage(), true, UserMapper.userResponseDTO(registeredOrganizer)));
    }

    @Operation(summary = "Register a new admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping(REGISTER_ADMIN)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerAdmin(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        User registeredAdmin = userService.registerAsAdmin(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(ADMIN_REGISTERED_SUCCESSFULLY.getMessage(), true, UserMapper.userResponseDTO(registeredAdmin)));
    }

    @Operation(summary = "Login using form-based authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping(FORM_LOGIN)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> formLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(LOGIN_SUCCESSFUL.getMessage(), true, UserMapper.userResponseDTO(user)));
    }

    @Operation(summary = "JWT login for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    @PostMapping(LOGIN)
    public ResponseEntity<ResponseDTO<LoginResponseDTO>> login(@RequestBody LoginRequestDTO requestDTO) {
        LoginResponseDTO response = userService.login(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).header(JWT_HEADER, response.jwtToken()).body(new ResponseDTO<>(LOGIN_SUCCESSFUL.getMessage(), true, response));
    }

    @Operation(summary = "Logout the currently logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful",
                    content = @Content(schema = @Schema(implementation = LogoutResponseDTO.class)))
    })
    @PostMapping(LOGOUT)
    public ResponseEntity<ResponseDTO<LogoutResponseDTO>> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.status(HttpStatus.OK).header(JWT_HEADER, "").body(new ResponseDTO<>(LOGOUT_SUCCESSFUL.getMessage(), true, new LogoutResponseDTO(email)));
    }

    @Operation(summary = "Fetch all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users fetched successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    })
    @GetMapping(GET_ALL_USERS)
    public ResponseEntity<ResponseDTO<List<UserResponseDTO>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> usersDTO = users.stream().map(UserMapper::userResponseDTO).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(ALL_USERS_FETCHED.getMessage(), true, usersDTO));
    }

    @Operation(summary = "Get a user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(GET_USER_BY_EMAIL)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        UserResponseDTO userDTO = UserMapper.userResponseDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(USER_FETCH_SUCCESS.getMessage(), true, userDTO));
    }

    @Operation(summary = "Check if a user exists by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User exists"),
            @ApiResponse(responseCode = "404", description = "User does not exist", content = @Content)
    })
    @GetMapping(CHECK_USER_EXISTS_BY_EMAIL)
    public ResponseEntity<ResponseDTO<Boolean>> checkUserExistsByEmail(@PathVariable String email) {
        boolean isValid = userService.existsByEmail(email);
        if (isValid) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(USER_EXISTS_WITH_EMAIL.getMessage(), true, null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>(String.format(EMAIL_NOT_EXISTS.getMessage(), email), true, null));
        }
    }

    @Operation(summary = "Get a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(GET_USER_BY_ID)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        UserResponseDTO userDTO = UserMapper.userResponseDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(USER_FETCH_SUCCESS.getMessage(), true, userDTO));
    }

    @Operation(summary = "Update user's name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User name updated",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PatchMapping(UPDATE_NAME)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> updateName(@PathVariable Long id, @RequestBody NameUpdateRequestDTO requestDTO) {
        User user = userService.updateName(id, requestDTO.name());
        UserResponseDTO userDTO = UserMapper.userResponseDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(USER_NAME_UPDATED_SUCCESSFULLY.getMessage(), true, userDTO));
    }

    @Operation(summary = "Update user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect old password", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PatchMapping(UPDATE_PASSWORD)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> updatePassword(@PathVariable Long id, @RequestBody ChangePasswordRequestDTO requestDTO) {
        User user = userService.updatePassword(id, requestDTO);
        UserResponseDTO userDTO = UserMapper.userResponseDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(PASSWORD_UPDATED_SUCCESSFULLY.getMessage(), true, userDTO));
    }

    @Operation(summary = "Trigger forgot password email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification email sent"),
            @ApiResponse(responseCode = "404", description = "User with email not found", content = @Content)
    })
    @PostMapping(FORGOT_PASSWORD)
    public ResponseEntity<ResponseDTO<?>> forgotPassword(@PathVariable String email) {
        userService.forgotPassword(email);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(PASSWORD_RESET_EMAIL_SENT.getMessage(), true, null));
    }

    @Operation(summary = "Verify email with code and change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successful",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Verification code expired or invalid", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PostMapping(VERIFY_EMAIL_AND_CHANGE_PASSWORD)
    public ResponseEntity<ResponseDTO<UserResponseDTO>> verifyEmailAndChangePassword(@RequestBody VerificationEmailAndPasswordChangeRequestDTO requestDTO) {
        User user = userService.verifyEmailAndChangePassword(requestDTO);
        UserResponseDTO userDTO = UserMapper.userResponseDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(PASSWORD_RESET_SUCCESSFUL.getMessage(), true, userDTO));
    }

    @Operation(summary = "Delete a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping(DELETE_USER_BY_ID)
    public ResponseEntity<ResponseDTO<?>> deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(USER_DELETED_SUCCESSFULLY.getMessage(), true, null));
    }

    @Operation(summary = "Delete a user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping(DELETE_USER_BY_EMAIL)
    public ResponseEntity<ResponseDTO<?>> deleteUserByEmail(@PathVariable String email) {
        userService.deleteByEmail(email);
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(USER_DELETED_SUCCESSFULLY.getMessage(), true, null));
    }
}
