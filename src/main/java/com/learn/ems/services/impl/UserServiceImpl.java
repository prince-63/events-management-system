package com.learn.ems.services.impl;

import com.learn.ems.dto.LoginRequestDTO;
import com.learn.ems.dto.LoginResponseDTO;
import com.learn.ems.dto.RegisterRequestDTO;
import com.learn.ems.entity.Role;
import com.learn.ems.entity.User;
import com.learn.ems.exceptions.InvalidPasswordException;
import com.learn.ems.exceptions.UnauthorizedRoleChangeException;
import com.learn.ems.exceptions.UserAlreadyExistsException;
import com.learn.ems.exceptions.UserNotFoundException;
import com.learn.ems.mapper.UserMapper;
import com.learn.ems.repositories.UserRepository;
import com.learn.ems.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.learn.ems.constants.AuthenticationConstants.JWT_SECRET_DEFAULT_VALUE;
import static com.learn.ems.constants.AuthenticationConstants.JWT_SECRET_KEY;
import static com.learn.ems.constants.ErrorMessageConstants.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticateManager;
    private final Environment environment;

    /**
     * Registers a new user with role ATTENDEE.
     *
     * @param requestDTO registration details
     * @return registered User object
     */
    @Override
    public User registerAsAttendee(RegisterRequestDTO requestDTO) {
        Optional<User> existingUserOpt = userRepository.findByEmail(requestDTO.email());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (existingUser.getRole().contains(Role.ADMIN)) {
                throw new UnauthorizedRoleChangeException(ADMIN_ROLE_CONFLICT.getMessage());
            }

            if (existingUser.getRole().contains(Role.ATTENDEE)) {
                throw new UserAlreadyExistsException(ATTENDEE_ALREADY_EXISTS.getMessage());
            }

            if (existingUser.getRole().contains(Role.ORGANIZER)) {
                if (!passwordEncoder.matches(requestDTO.password(), existingUser.getPassword())) {
                    throw new InvalidPasswordException(ORGANIZER_PASSWORD_MISMATCH.getMessage());
                }

                existingUser.getRole().add(Role.ATTENDEE);
                return userRepository.save(existingUser);
            }
        }

        // Create new user
        User newAttendee = UserMapper.toModel(requestDTO);
        newAttendee.setPassword(passwordEncoder.encode(requestDTO.password()));
        newAttendee.setRole(Set.of(Role.ATTENDEE));
        newAttendee.setEnabled(true);
        return userRepository.save(newAttendee);
    }


    /**
     * Registers a new user with role ADMIN.
     *
     * @param requestDTO registration details
     * @return registered User object
     */
    @Override
    public User registerAsAdmin(RegisterRequestDTO requestDTO) {
        Optional<User> existingUserOpt = userRepository.findByEmail(requestDTO.email());

        if (existingUserOpt.isPresent()) {
            throw new UserAlreadyExistsException(ADMIN_ALREADY_EXISTS.getMessage());
        }

        User newAdmin = UserMapper.toModel(requestDTO);
        newAdmin.setPassword(passwordEncoder.encode(requestDTO.password()));
        newAdmin.setRole(Set.of(Role.ADMIN));
        newAdmin.setEnabled(true);
        return userRepository.save(newAdmin);
    }

    /**
     * Registers a new user with role ORGANIZER.
     *
     * @param requestDTO registration details
     * @return registered User object
     */
    @Override
    public User registerAsOrganizer(RegisterRequestDTO requestDTO) {
        Optional<User> existingUserOpt = userRepository.findByEmail(requestDTO.email());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (existingUser.getRole().contains(Role.ADMIN)) {
                throw new UnauthorizedRoleChangeException(ADMIN_ROLE_CONFLICT.getMessage());
            }

            if (existingUser.getRole().contains(Role.ORGANIZER)) {
                throw new UserAlreadyExistsException(ORGANIZER_ALREADY_EXISTS.getMessage());
            }

            if (existingUser.getRole().contains(Role.ATTENDEE)) {
                if (!passwordEncoder.matches(requestDTO.password(), existingUser.getPassword())) {
                    throw new InvalidPasswordException(ATTENDEE_PASSWORD_MISMATCH.getMessage());
                }

                existingUser.getRole().add(Role.ORGANIZER);
                return userRepository.save(existingUser);
            }
        }

        // Create new organizer
        User newOrganizer = UserMapper.toModel(requestDTO);
        newOrganizer.setPassword(passwordEncoder.encode(requestDTO.password()));
        newOrganizer.setRole(Set.of(Role.ORGANIZER));
        newOrganizer.setEnabled(true);
        return userRepository.save(newOrganizer);
    }

    /**
     * Authenticates a user using email and password.
     *
     * @param requestDTO login request DTO
     * @return authenticated User object
     */
    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        String jwt;
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(requestDTO.email(), requestDTO.password());
        Authentication authenticationResponse =  authenticateManager.authenticate(authentication);
        if (authenticationResponse != null) {
            String secret = environment.getProperty(JWT_SECRET_KEY, JWT_SECRET_DEFAULT_VALUE);
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            jwt = Jwts.builder()
                    .issuer("bookstore").subject("JWT Token")
                    .claim("username", authenticationResponse.getPrincipal())
                    .claim("authorities", authenticationResponse.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime() + 30000000))
                    .signWith(secretKey).compact();
            return new LoginResponseDTO(jwt);
        }

        return new LoginResponseDTO("");
    }

    /**
     * Retrieves all users in the system.
     *
     * @return list of users
     */
    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    /**
     * Finds a user by their email address.
     *
     * @param email unique email
     * @return matching User object
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(String.format(EMAIL_NOT_EXISTS.getMessage(), email)));
    }

    /**
     * Checks if a user exists with the given email.
     *
     * @param email unique email
     * @return true if user exists, false otherwise
     */
    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id unique user ID
     * @return User object
     */
    @Override
    public User getById(Long id) {
        return null;
    }

    /**
     * Updates the user's name.
     *
     * @param id   user ID
     * @param name new name to update
     * @return updated User object
     */
    @Override
    public User updateName(Long id, String name) {
        return null;
    }

    /**
     * Changes the password after validating the previous password.
     *
     * @param id               user ID
     * @param previousPassword current password
     * @param newPassword      new password to set
     * @return updated User object
     */
    @Override
    public User updatePassword(Long id, String previousPassword, String newPassword) {
        return null;
    }

    /**
     * Initiates the forgot password process (sends verification link or code).
     *
     * @param email email address of user
     * @return User object (or throw exception if not found)
     */
    @Override
    public User forgotPassword(String email) {
        return null;
    }

    /**
     * Verifies a one-time code and updates the password.
     *
     * @param verificationCode code sent to email
     * @param newPassword      new password to set
     * @return updated User object
     */
    @Override
    public User verifyEmailAndChangePassword(String verificationCode, String newPassword) {
        return null;
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id user ID
     */
    @Override
    public void deleteById(Long id) {

    }

    /**
     * Deletes a user by their email.
     *
     * @param email user's email address
     */
    @Override
    public void deleteByEmail(String email) {

    }
}
