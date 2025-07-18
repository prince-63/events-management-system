package com.learn.ems.services.impl;

import com.learn.ems.entity.User;
import com.learn.ems.exceptions.UserNotFoundException;
import com.learn.ems.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.learn.ems.constants.ErrorMessageConstants.EMAIL_NOT_EXISTS;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        User dbUser = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException(String.format(EMAIL_NOT_EXISTS, username)));
        List<GrantedAuthority> authorities = dbUser.getRole().stream().map((role) -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(dbUser.getEmail(), dbUser.getPassword(), true, true, true, true, authorities);
    }
}
