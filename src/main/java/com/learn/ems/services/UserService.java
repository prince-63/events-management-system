package com.learn.ems.services;

import com.learn.ems.dto.*;
import com.learn.ems.entity.User;

import java.util.List;

/**
 * Service interface for managing user registration, authentication, and profile operations.
 */
public interface UserService {

    /**
     * Registers a new user with role ATTENDEE.
     *
     * @param requestDTO registration details
     * @return registered User object
     */
    User registerAsAttendee(RegisterRequestDTO requestDTO);

    /**
     * Registers a new user with role ADMIN.
     *
     * @param requestDTO registration details
     * @return registered User object
     */
    User registerAsAdmin(RegisterRequestDTO requestDTO);

    /**
     * Registers a new user with role ORGANIZER.
     *
     * @param requestDTO registration details
     * @return registered User object
     */
    User registerAsOrganizer(RegisterRequestDTO requestDTO);

    /**
     * Authenticates a user using email and password.
     *
     * @param requestDTO login request DTO
     * @return authenticated User object
     */
    LoginResponseDTO login(LoginRequestDTO requestDTO);

    /**
     * Retrieves all users in the system.
     *
     * @return list of users
     */
    List<User> getAllUsers();

    /**
     * Finds a user by their email address.
     *
     * @param email unique email
     * @return matching User object
     */
    User findByEmail(String email);

    /**
     * Checks if a user exists with the given email.
     *
     * @param email unique email
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves a user by their ID.
     *
     * @param id unique user ID
     * @return User object
     */
    User getById(Long id);

    /**
     * Updates the user's name.
     *
     * @param id user ID
     * @param name new name to update
     * @return updated User object
     */
    User updateName(Long id, String name);

    /**
     * Changes the password after validating the previous password.
     *
     * @param id user ID
     * @param requestDTO - containing new password, and curr password
     * @return updated User object
     */
    User updatePassword(Long id, ChangePasswordRequestDTO requestDTO);

    /**
     * Initiates the forgot password process (sends verification link or code).
     *
     * @param email email address of user
     * @return User object (or throw exception if not found)
     */
    void forgotPassword(String email);

    /**
     * Verifies a one-time code and updates the password.
     *
     * @param requestDTO containing all the request data from the user like there email, new password, verification code.
     * @return updated User object
     */
    User verifyEmailAndChangePassword(VerificationEmailAndPasswordChangeRequestDTO requestDTO);

    /**
     * Deletes a user by their ID.
     *
     * @param id user ID
     */
    void deleteById(Long id);

    /**
     * Deletes a user by their email.
     *
     * @param email user's email address
     */
    void deleteByEmail(String email);
}

