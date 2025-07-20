package com.learn.ems.config;

import com.learn.ems.filters.JwtTokenGeneratorFilter;
import com.learn.ems.filters.JwtTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.learn.ems.constants.EventApiEndPointsConstants.*;
import static com.learn.ems.constants.RegistrationApiEndPointsConstants.*;
import static com.learn.ems.constants.SwaggerEndPointsConstants.SWAGGER_WHITELIST;
import static com.learn.ems.constants.UserApiEndPointsConstants.*;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(
                        (req) -> req
                                .requestMatchers(
                                        REGISTER_ATTENDEE,
                                        REGISTER_ADMIN,
                                        REGISTER_ORGANIZER,
                                        LOGIN,
                                        LOGOUT
                                ).permitAll()
                                .requestMatchers(
                                        GET_ALL_USERS,
                                        CHECK_USER_EXISTS_BY_EMAIL,
                                        DELETE_USER_BY_EMAIL
                                ).hasRole("ADMIN")
                                .requestMatchers(
                                        CREATE_EVENT, UPDATE_EVENT,
                                        UPLOAD_BANNER, DELETE_EVENT
                                ).hasRole("ORGANIZER")
                                .requestMatchers(
                                        GET_CURRENT_USER,
                                        UPDATE_NAME,
                                        UPDATE_PASSWORD,
                                        FORGOT_PASSWORD,
                                        VERIFY_EMAIL_AND_CHANGE_PASSWORD,
                                        GET_ALL_EVENTS,
                                        GET_EVENT_BY_ID,
                                        GET_EVENTS_BY_ORGANIZER,
                                        GET_EVENTS_BY_TITLE,
                                        REGISTER_EVENT,
                                        CANCEL_REGISTER_EVENT,
                                        CHECK_IN_USER,
                                        GET_REGISTERED_EVENTS,
                                        GET_REGISTERED_EVENT_BY_USER,
                                        GET_REGISTERED_EVENT_DETAILS
                                ).authenticated()
                                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                                .anyRequest().denyAll());
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        UsernamePasswordAuthenticationProvider authenticationProvider = new UsernamePasswordAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }
}
