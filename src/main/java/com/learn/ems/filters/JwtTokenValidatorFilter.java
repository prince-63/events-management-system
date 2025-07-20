package com.learn.ems.filters;

import com.learn.ems.exceptions.ForbiddenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.learn.ems.constants.AuthenticationConstants.*;
import static com.learn.ems.constants.ErrorMessageConstants.JWT_TOKEN_NOT_VALID;
import static com.learn.ems.constants.UserApiEndPointsConstants.*;

public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JWT_HEADER);

        if (jwt != null) {
            try {
                Environment env = getEnvironment();
                String secret = env.getProperty(JWT_SECRET_KEY, JWT_SECRET_DEFAULT_VALUE);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();
                String username = String.valueOf(claims.get("username"));
                String authorities =  String.valueOf(claims.get("authorities"));
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch (Exception e) {
                throw new ForbiddenException(JWT_TOKEN_NOT_VALID);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<String> ignoreValidationUri = List.of(FORM_LOGIN, LOGIN, REGISTER_ATTENDEE, REGISTER_ORGANIZER, REGISTER_ADMIN);
        for (String uri : ignoreValidationUri) {
            if (request.getRequestURI().equals(uri)) {
                return true;
            }
        }
        return false;
    }
}
