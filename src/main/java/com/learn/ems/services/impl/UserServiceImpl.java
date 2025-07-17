package com.learn.ems.services.impl;

import com.learn.ems.dto.*;
import com.learn.ems.entity.Role;
import com.learn.ems.entity.User;
import com.learn.ems.exceptions.InvalidPasswordException;
import com.learn.ems.exceptions.UnauthorizedRoleChangeException;
import com.learn.ems.exceptions.UserAlreadyExistsException;
import com.learn.ems.exceptions.UserNotFoundException;
import com.learn.ems.mapper.UserMapper;
import com.learn.ems.repositories.UserRepository;
import com.learn.ems.services.EmailService;
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
import java.time.LocalDateTime;
import java.util.*;
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
    private final EmailService emailService;

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

        User newAttendee = UserMapper.toModel(requestDTO);
        newAttendee.setPassword(passwordEncoder.encode(requestDTO.password()));
        newAttendee.setRole(Set.of(Role.ATTENDEE));
        newAttendee.setEnabled(true);
        return userRepository.save(newAttendee);
    }

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

        User newOrganizer = UserMapper.toModel(requestDTO);
        newOrganizer.setPassword(passwordEncoder.encode(requestDTO.password()));
        newOrganizer.setRole(Set.of(Role.ORGANIZER));
        newOrganizer.setEnabled(true);
        return userRepository.save(newOrganizer);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        String jwt;
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(requestDTO.email(), requestDTO.password());
        Authentication authenticationResponse = authenticateManager.authenticate(authentication);
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

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(String.format(EMAIL_NOT_EXISTS.getMessage(), email)));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_EXISTS.getMessage(), id)));
    }

    @Override
    public User updateName(Long id, String name) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_EXISTS.getMessage(), id)));
        user.setName(name);
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(Long id, ChangePasswordRequestDTO requestDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_EXISTS.getMessage(), id)));
        if (passwordEncoder.matches(requestDTO.currentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(requestDTO.newPassword()));
            return userRepository.save(user);
        } else {
            throw new InvalidPasswordException(CHANGE_PASSWORD_MISMATCH.getMessage());
        }
    }

    @Override
    public void forgotPassword(String email) {
        User dbUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format(EMAIL_NOT_EXISTS.getMessage(), email)));
        dbUser.setPwdVerfCode(generateVerificationCode());
        dbUser.setPwdVerfDur(LocalDateTime.now().plusMinutes(5));

        emailService.sendEmail(
                email,
                "Password Reset Code – EMS 🔐",
                "<h1>Hello " + dbUser.getName() + "!</h1>" +
                        "<p>Your password reset code is <b>" + dbUser.getPwdVerfCode() + "</b>. It will expire in 5 minutes.</p>"
        );
        userRepository.save(dbUser);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @Override
    public User verifyEmailAndChangePassword(VerificationEmailAndPasswordChangeRequestDTO requestDTO) {
        String email = requestDTO.email();
        String newPassword = requestDTO.newPassword();
        String verificationCode = requestDTO.verificationCode();

        User dbUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format(EMAIL_NOT_EXISTS.getMessage(), email)));

        if (!verificationCode.equals(dbUser.getPwdVerfCode())) {
            throw new RuntimeException("Invalid verification code.");
        }

        if (dbUser.getPwdVerfDur() == null || dbUser.getPwdVerfDur().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code has expired.");
        }

        dbUser.setPassword(passwordEncoder.encode(newPassword));
        dbUser.setPwdVerfCode(null);
        dbUser.setPwdVerfDur(null);

        return userRepository.save(dbUser);
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_EXISTS.getMessage(), id)));
        userRepository.delete(user);
    }

    @Override
    public void deleteByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(String.format(EMAIL_NOT_EXISTS.getMessage(), email)));
        userRepository.delete(user);
    }
}
